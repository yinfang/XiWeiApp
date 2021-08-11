package com.zyf.model;

import java.io.Serializable;

public class Point implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8252237310350864258L;

	public String name;
	public double longitude;
	public double latitude;

	/**
	 * 
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 */
	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 *
	 * @param latitude
	 *            纬度
	 * @param longitude
	 *            经度
	 * @param name
	 *            地点的名称
	 */

	public Point(double latitude, double longitude, String name) {
		this(latitude, longitude);
		this.name = name;
	}
}
