package com.visenze.demo.feature.server;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.listen.ListenerContainer;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by lei on 2/16/14.
 */
public class FeatureService extends LeaderSelectorListenerAdapter implements PathChildrenCacheListener {

    private static String PATH_LEADER = "/feature_leader";
    private static String PATH_MEMBER = "/feature_member";


    private final CuratorFramework cclient;
    /**
     * whether is master, default is false
     */
    private boolean isMaster = false;

    private LeaderSelector election = null;
    private Set<PathChildrenCache> memberpathcaches = new HashSet<PathChildrenCache>();
    private ListenerContainer<MemberUpdateListener> memberChangelisteners = new ListenerContainer<MemberUpdateListener>();
    private Set<ServiceInstance> members = new HashSet<ServiceInstance>();
    private final ExecutorService executor;
    private final InstanceContext context;

    public FeatureService(boolean leaderElection, InstanceContext context, String zkConnectionString) {
        this.context = context;
        if (context.getPort() == -1) {
            throw new RuntimeException("must specified the instance port before get the ServiceCenterClient");
        }
        executor = Executors.newCachedThreadPool();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        cclient = CuratorFrameworkFactory.newClient(zkConnectionString, retryPolicy);
        cclient.start();
        if (leaderElection) {
            this.startLeaderElection();
        }
        this.registerMyself();
        this.startMemberChangeListener();
    }

    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        System.out.println(context.getHostName() + ":" + context.getPort() + " is now the leader.");
        isMaster = true;
        synchronized (election) {
            election.wait();
        }
    }

    private void startLeaderElection() {
        election = new LeaderSelector(cclient, PATH_LEADER, executor, this);
        election.autoRequeue();
        election.start();
    }

    private void registerMyself() {
        try {
            String hostName = context.getHostName();
            int port = context.getPort();
            String nodeName = hostName + ":" + port;

            Stat state = cclient.checkExists().forPath(PATH_MEMBER);
            if (state == null) {
                cclient.create().forPath(PATH_MEMBER);
            }

            String path = PATH_MEMBER + "/" + nodeName;
            state = cclient.checkExists().forPath(path);
            if (state == null) {
                cclient.create().withMode(CreateMode.EPHEMERAL).forPath(path);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startMemberChangeListener() {
        try {
            List<String> childs = cclient.getChildren().forPath(PATH_MEMBER);
            if (childs != null) {
                for (String child : childs) {
                    ServiceInstance instance = new ServiceInstance(child);
                    members.add(instance);
                }
            }
            PathChildrenCache memberpathcache = new PathChildrenCache(cclient, PATH_MEMBER, true);
            memberpathcache.start();
            memberpathcache.getListenable().addListener(this);
            memberpathcaches.add(memberpathcache);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        System.out.println("childEvent");
        switch (event.getType()) {
            case CHILD_ADDED: {
                String path = event.getData().getPath();
                if (members.add(new ServiceInstance(path))) {
                    applyMemberChangeListener();
                }
                break;
            }

            case CHILD_UPDATED: {
                break;
            }

            case CHILD_REMOVED: {
                String path = event.getData().getPath();
                if (members.remove(new ServiceInstance(path))) {
                    applyMemberChangeListener();
                }
                break;
            }
        }
    }

    private void applyMemberChangeListener() {
        this.memberChangelisteners.forEach(
                new Function<MemberUpdateListener, Void>() {
                    public Void apply(MemberUpdateListener listener) {
                        try {
                            listener.handle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
        );
    }

}
