package com.hqu.utils;

public class Distance {
	private static double EARTH_RADIUS = 6378.137;//KM
	private static double rad(double d) { 
        return d * Math.PI / 180.0; 
    }
	/**
     * @param lat1 经度
     * @param lng1 纬度
     * @param lat2 经度
     * @param lng2 纬度
     * @return
     */
	public static String getDistance(Double lat1, Double lng1, Double lat2, Double lng2) {

		double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = distance * 100 ;//精确到10米之内比较大小。
        String distanceStr = distance+"";
        distanceStr = distanceStr.substring(0, distanceStr.indexOf("."));
         
        return distanceStr;
    }
	public static void main(String[] args){
		
		System.out.println(getDistance(117.11811,36.68484,117.00999000000002,36.66123));
	}
}
