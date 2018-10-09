package com.tzj.baselib.service.socket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 单个socket管理
 */
public class MClientSocket {
	protected final boolean DEBAG=false;
	protected Connect connectThread;
	private String threadName="未知";
	
	public MClientSocket(String name) {
		threadName = name; 
	}
	
	/**
	 * 会断开以前的连接
	 * @return boolean 成功与否
	 */
	public synchronized boolean connect(final String address,final int port){
		if (connectThread!=null) {
			connectThread.cancel();
		}
		try {
			connectThread = new Connect(address, port, 5000);
			connectThread.start();
		} catch (IOException e) {
			logErr(e);
			connectThread = null;
			return false;
		}
		return true;
	}
	/**
	 * 关闭连接
	 */
	public synchronized void close(){
		if (connectThread!=null) {
			connectThread.write("exit\r\n".getBytes());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			connectThread.cancel();
		}
	}
	/**
	 * 状态-1(关闭),1(正常连接),0(绑定)
	 */
	public int getState(){
		if (connectThread==null||connectThread.so.isClosed()) {
			return -1;//关闭
		}
		if (connectThread.so.isConnected()) {
			return 1;//正常连接
		}
		if (connectThread.so.isBound()) {
			return 0;//绑定
		}
		return -1;
	}
	/**
	 * 写
	 */
	public synchronized void write(byte[] bs){
		try {
			if (connectThread!=null) {
				connectThread.write(bs);
			}
		} catch (Exception e) {
			logErr(e);
			connectThread.cancel();
		}
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
	
	/**
	 * 连接然后读写线程
	 */
	private class Connect extends Thread{
		private Socket so = new Socket();
		private OutputStream os;
		private InputStream is;
		public Connect(String ip,int port,int outTime) throws IOException {
			synchronized (MClientSocket.this) {
				SocketAddress endpoint = new InetSocketAddress(ip, port);
				so.connect(endpoint,outTime);
				os = so.getOutputStream();
				is = so.getInputStream();
			}
		}
		@Override
		public void run() {
			super.run();
			setName(threadName);
            while (is!=null) {
            	try {
            		byte[] read = readRule(is);
            		logRed(new String(read,0,read.length-2));
            		Set<Entry<String,SocketRet>> entrySet = mListener.entrySet();
            		for (Entry<String,SocketRet> e:entrySet) {
            			try {
							e.getValue().ret(read);
						} catch (Exception e1) {
							logErr(e1);
						}
					}
				} catch (Exception e) {
					logErr(e);
					cancel();
				}
            }
		}
		public void write(byte[] bs){
			try {
				os.write(bs);
				os.flush();
				log("发送："+new String(bs,0,bs.length-2));
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
					Set<Entry<String,SocketRet>> entrySet = mListener.entrySet();
            		for (Entry<String,SocketRet> e:entrySet) {
            			e.getValue().err();
					}
				}
			} catch (IOException e1) {
				logErr(e1);
			}
			connectThread = null;
		}
	}
	
	//回调listener
	//========================================
	private Map<String, SocketRet> mListener = new HashMap<>();
	public interface SocketRet{
		/**读到网络数据的回调*/
		void ret(byte[] bs);
		/** 断开的回调 */
		void err();
	}
	public synchronized void addListener(String name,SocketRet sr){
		mListener.put(name, sr);
	}
	public synchronized void removeListener(String name){
		if (name!=null) {
			mListener.remove(name);
		}else{
			mListener.clear();
		}
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
			throw new RuntimeException(e);
		}else{
			e.printStackTrace();
		}
	}
	
}

