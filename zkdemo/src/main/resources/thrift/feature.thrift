namespace java me.gaolei.demo.zk.thrift


service FeatureThriftService {
	void extractFeature(),	
	string getHostPort(),
	void ping()
	
}