package com.tzj.baselib.service.socket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class MServiceSocket {
	
	protected final boolean DEBAG=false;
	
	private boolean runing = false;
	private ServerSocket so;
	private Map<String, Connect> map = new HashMap<>();
	private SocketRet sor;
	
	public MServiceSocket(int point) throws IOException {
		so = new ServerSocket(point);
		sor = new SocketRet() {
			@Override
			public void ret(String name, byte[] bs) {
				Set<Entry<String,Connect>> entrySet = map.entrySet();
				for (Entry<String,Connect>e:entrySet) {
					if (!e.getKey().equals(name)) {
						e.getValue().write(e.getKey(),bs);
					}
				}
				if ("exit\r\n".equals(new String(bs))) {
					map.get(name).cancel();
					return;
				}
			}
			@Override
			public void err(String name) {
				log(name+"：出错");
			}
		};
	}
	
	public void setSocketRet(SocketRet sor){
		this.sor=sor;
	}
	
	public void start() throws Exception {
		log("监听："+so.getLocalPort());
		runing=true;
		while (runing) {
			Socket s = so.accept();
			Connect connect = new Connect(s, s.hashCode()+"", sor);
			connect.start();
		}
	}
	
	public synchronized void writeAll(byte[] bs){
		Set<Entry<String,Connect>> entrySet = map.entrySet();
		for (Entry<String,Connect>e:entrySet) {
			try {
				e.getValue().write(e.getKey(),bs);
			} catch (Exception e1) {
			}
		}
	}
	
	public void stop(){
		runing=false;
		Set<Entry<String,Connect>> entrySet = map.entrySet();
		for (Entry<String,Connect>e:entrySet) {
			e.getValue().cancel();
		}
		try {
			so.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 连接然后读写线程
	 */
	private class Connect extends Thread{
		
		private String name;
		private Socket so;
		private OutputStream os;
		private InputStream is;
		private SocketRet sor;
		
		public Connect(Socket so,String name,SocketRet sor) throws IOException {
			this.so = so;
			this.name = name;
			this.sor = sor;
			os = so.getOutputStream();
			is = so.getInputStream();
			map.put(name, this);
			log("新连接："+name);
		}
		@Override
		public void run() {
			super.run();
			setName("Client："+name);
            while (is!=null) {
            	try {
            		byte[] read = readRule(is);
            		log("来自"+name+" ："+new String(read,0,read.length-2));
        			try {
        				if (sor!=null) {
        					sor.ret(name,read);
						}
					} catch (Exception e1) {
						logErr(e1);
					}
				} catch (Exception e) {
					logErr(e);
					cancel();
				}
            }
		}
		public void write(String name,byte[] bs){
			try {
				os.write(bs);
				os.flush();
				logRed("发送给"+name+"："+new String(bs,0,bs.length-2));
			} catch (IOException e) {
				logErr(e);
				cancel();
			}
		}
		/** 关闭*/
		public synchronized void cancel() {
			try {
				if (is!=null) {
					is.close();
					is=null;
				}
				if (os !=null) {
					os.flush();
					os.close();
					os=null;
				}
				if (so!=null) {
					so.close();
					so = null;
            		if (sor!=null) {
						sor.err(name);
					}
				}
			} catch (IOException e1) {
				logErr(e1);
			}finally{
				log(name+"：退出");
				map.remove(name);
			}
		}
	}
	
	public interface SocketRet{
		/**读到网络数据的回调*/
		void ret(String name, byte[] bs);
		/** 断开的回调 */
		void err(String name);
	}
	/** 读的一次结束的规则(这里是以\r\n)*/
	protected byte[] readRule(InputStream is) throws IOException {
		int c = 0;
		int temp = 0;
		List<Byte> list = new ArrayList<>();
		while (temp!='\r'||c!='\n') {
			temp = c;
			c = is.read();
			list.add((byte)c);
		}
		byte[] bs =new byte[list.size()];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = list.get(i);
		}
		return bs;
	}
	
	//log
	//=====================================
	/**
	 * 打印
	 */
	private void log(String str){
		if (DEBAG) {
			System.out.println(str);
		}
	}
	/**
	 * 打印
	 */
	private void logRed(String str){
		if (DEBAG) {
			System.err.println(str);
		}
	}
	/**
	 * 打印
	 */
	private void log(byte[] str){
		if (DEBAG) {
			try {
				System.out.print(new String(str,"utf-8"));
			} catch (UnsupportedEncodingException e) {
				try {
					System.out.print(new String(str,"gbk"));
				} catch (UnsupportedEncodingException e1) {
					System.out.print(new String(str));
				}
			}
		}
	}
	/**
	 * 打印
	 */
	private void logErr(Exception e){
		if (DEBAG) {
//			throw new RuntimeException(e);
			e.printStackTrace();
		}else{
			e.printStackTrace();
		}
	}
	
}
