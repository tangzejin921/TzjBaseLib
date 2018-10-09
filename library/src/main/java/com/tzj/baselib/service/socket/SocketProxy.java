package com.tzj.baselib.service.socket;



public class SocketProxy {
	private MClientSocket cs;
	private MServiceSocket ss;
	
	public SocketProxy(final String address,final int proxyPort,final int port) {
		cs = new MClientSocket("SocketProxy");
		cs.addListener("test", new MClientSocket.SocketRet() {
			@Override
			public void ret(byte[] bs) {
				if (ss!=null) {
					ss.writeAll(bs);
				}
			}
			@Override
			public void err() {
			}
		});
		boolean connect = cs.connect(address, proxyPort);
		try {
			if (connect) {
				ss = new MServiceSocket(port);
				ss.setSocketRet(new MServiceSocket.SocketRet() {
					@Override
					public void ret(String name, byte[] bs) {
//						String string = new String(bs);
//						if (string.startsWith("Host")) {
//							cs.write(("Host: "+address+":"+proxyPort+"\r\n").getBytes());
//						}else{
//							cs.write(bs);
//						}
						cs.write(bs);
					}
					@Override
					public void err(String name) {
						
					}
				});
				ss.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		//ws://smtws.xnfirm.com:8080/API/HouseCtrl
		//smtws.xnfirm.com:8080
		new SocketProxy("smtws.xnfirm.com", 8080,8080);
	}
	
	
	
	
	
	
	
	
	
}
