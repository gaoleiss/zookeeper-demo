zookeeper-demo
==============

##1. zk-env: zookeeper集群， 由三个zookeeper实例组成。##


## 2. zkdemo: zookeeper操作代码 ##
### zookeeper node 设计###
```
  * /master(thrift_host:port)
  * /workers
      * /thrift_host:port
      * /thrift_host:port
```
master节点的数据是 thrift host 和 thrift port, workers 节点是所有个计算单元。

	 	 	
###1. 所有feature job服务(feature job的实际业务不需要实现) 启动能自动注册信息到zk上，并保存配置信息在zk上。 ###
### 2. 从所有注册的job服务中选举一个服务作为master 节点。###
```
private void register() {
        String path = Variable.WORK_PATH + "/" + serverId;

        try {
            zk.create(path, "idle".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createWorkerCallback, null);
            masterSelector.update(serverId);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```
每个feature job 启动的时候都先注册到zk上，然后update master节点：检测现有master节点的是否存在

```
void setMasterServerId(String serverId) throws KeeperException, InterruptedException {
        zk.setData(Variable.MASTER_PATH, serverId.getBytes(), -1);
        System.out.println("master update:\t" + serverId);
    }

    boolean isMasterExists() throws KeeperException, InterruptedException {
        boolean ret = false;
        String masterServerId = getMaster();

        for (String child : zk.getChildren(Variable.WORK_PATH, false)) {
            if (child.equals(masterServerId)) {
                return true;
            }
        }

        return ret;
    }

    public void update(String serverId) throws KeeperException, InterruptedException {
        if (!isMasterExists()) {
            setMasterServerId(serverId);
        }

    }
```
	

###3. Client 向master发送一个request请求，需要一个计算单元，master得到一个最优的计算单元告诉
client，然后client再直接和此计算单元通信执行计算操作。 ###
feature client 运行过程如下：
```
 public void process() throws Exception {
        // get master in zookeeper
        String master = masterSelector.getMaster();
        // get the job thrift hostport from master thrift server
        String thriftHostPort = thriftClient.getHostPort(master);
        // extract feature
        thriftClient.extractFeature(thriftHostPort);
        System.out.println("extract feature from " + thriftHostPort);
    }
```
先从连接master job拿到最优的节点，然后在连接这个最优的节点 extrat feature

	4. 如果master 节点服务停止，系统能自动从现有节点中选择出一个新的节点作为master。 （10分）

	5. master 选择feature job给client使用的策略. 最低要求：只实现一个从现有可用的job中随机选取的策略 (10分)（更优的选择策略最多有10分加分）

	6. 代码整洁，模块划分清晰，核心代碼单元测试代码覆盖率要超過50%, 提交具體數值和證據（10分）。 

	7. 将代码整理好，放到自己的git repository中，java工程必须是maven工程。保证代码是可以run的 (10分）
	
