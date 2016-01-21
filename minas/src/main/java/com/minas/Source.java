package com.minas;

/**
 * @author armansimonyan
 */
public enum Source {

	/**
	 * Read from RAMLruCache
	 */
	RAM,

	/**
	 * Read from DiskLruCache
	 */
	DISK,

	/**
	 * Read from Downloader
	 */
	NETWORK

}
