root_dir=$PWD

cd $root_dir/zookeeper-1/bin/
./zkServer.sh start

cd $root_dir/zookeeper-2/bin/
./zkServer.sh start

cd $root_dir/zookeeper-3/bin/
./zkServer.sh start
