package com.hitzd.his.adr.mr;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public class MrAgent
{
	public synchronized  int get_file(String Host, String RemoteFile, String LocalFile, int Option)
	{
		return CLibrary.INSTANCE.get_file(Host, RemoteFile, LocalFile, Option);
	}
	
	private static abstract interface CLibrary extends StdCallLibrary 
	{
		public static final CLibrary INSTANCE = (CLibrary) Native.loadLibrary("fsrv", CLibrary.class);
		public abstract int get_file(String Host, String RemoteFile, String LocalFile, int Option);
	}
}

