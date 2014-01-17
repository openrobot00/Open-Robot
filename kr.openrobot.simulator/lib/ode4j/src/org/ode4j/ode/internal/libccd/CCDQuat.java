/***
 * libccd
 * ---------------------------------
 * Copyright (c)2010 Daniel Fiser <danfis@danfis.cz>
 * Java-port: Copyright (c) 2007-2012 Tilmann Zäschke <ode4j@gmx.de>  
 *
 *
 *  This file is part of libccd.
 *
 *  Distributed under the OSI-approved BSD License (the "License");
 *  see accompanying file BDS-LICENSE for details or see
 *  <http://www.opensource.org/licenses/bsd-license.php>.
 *
 *  This software is distributed WITHOUT ANY WARRANTY; without even the
 *  implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the License for more information.
 */
package org.ode4j.ode.internal.libccd;

import static org.ode4j.ode.internal.libccd.CCDVec3.*;

public class CCDQuat {

	public static class ccd_quat_t {
		//TZ: fields are much faster than arrays.
	    double q0, q1, q2, q3; //!< x, y, z, w
	    public void set(double x, double y, double z, double w) {
	    	q0 = x;
	    	q1 = y;
	    	q2 = z;
	    	q3 = w;
	    }
		public Object get0() {
			return q0;
		}
		public Object get1() {
			return q1;
		}
		public Object get2() {
			return q2;
		}
		public Object get3() {
			return q3;
		}
	};


	/**** INLINES ****/
	static final double ccdQuatLen2(final ccd_quat_t q)
	{
	    double len;

	    len  = q.q0 * q.q0;
	    len += q.q1 * q.q1;
	    len += q.q2 * q.q2;
	    len += q.q3 * q.q3;

	    return len;
	}

	static final double ccdQuatLen(final ccd_quat_t q)
	{
	    return CCD_SQRT(ccdQuatLen2(q));
	}

	public static final void ccdQuatSet(ccd_quat_t q, double x, double y, double z, double w)
	{
	    q.q0 = x;
	    q.q1 = y;
	    q.q2 = z;
	    q.q3 = w;
	}

	static final void ccdQuatCopy(ccd_quat_t dest, final ccd_quat_t src)
	{
		//*dest = *src;
	    dest.q0 = src.q0;
	    dest.q1 = src.q1;
	    dest.q2 = src.q2;
	    dest.q3 = src.q3;
	}


	static final int ccdQuatNormalize(ccd_quat_t q)
	{
	    double len = ccdQuatLen(q);
	    if (len < CCD_EPS)
	        return 0;

	    ccdQuatScale(q, CCD_ONE / len);
	    return 1;
	}

	public static final void ccdQuatSetAngleAxis(ccd_quat_t q,
	                                     double angle, final ccd_vec3_t axis)
	{
	    double a, x, y, z, n, s;

	    a = angle/2;
	    x = ccdVec3X(axis);
	    y = ccdVec3Y(axis);
	    z = ccdVec3Z(axis);
	    n = CCD_SQRT(x*x + y*y + z*z);

	    // axis==0? (treat this the same as angle==0 with an arbitrary axis)
	    if (n < CCD_EPS){
	        q.q0 = q.q1 = q.q2 = CCD_ZERO;
	        q.q3 = CCD_ONE;
	    }else{
	        s = Math.sin(a)/n;

	        q.q3 = Math.cos(a);
	        q.q0 = x*s;
	        q.q1 = y*s;
	        q.q2 = z*s;

	        ccdQuatNormalize(q);
	    }
	}


	static final void ccdQuatScale(ccd_quat_t q, double k)
	{
		q.q0 *= k;
		q.q1 *= k;
		q.q2 *= k;
		q.q3 *= k;
	}

	
	/**
	 * q = q * q2
	 */
	public static final void ccdQuatMul(ccd_quat_t q, final ccd_quat_t q2)
	{
	    ccd_quat_t a = new ccd_quat_t();
	    ccdQuatCopy(a, q);
	    ccdQuatMul2(q, a, q2);
	}

	/**
	 * q = a * b
	 */
	static final void ccdQuatMul2(ccd_quat_t q,
	                             final ccd_quat_t a, final ccd_quat_t b)
	{
	    q.q0 = a.q3 * b.q0
	                + a.q0 * b.q3
	                + a.q1 * b.q2
	                - a.q2 * b.q1;
	    q.q1 = a.q3 * b.q1
	                + a.q1 * b.q3
	                - a.q0 * b.q2
	                + a.q2 * b.q0;
	    q.q2 = a.q3 * b.q2
	                + a.q2 * b.q3
	                + a.q0 * b.q1
	                - a.q1 * b.q0;
	    q.q3 = a.q3 * b.q3
	                - a.q0 * b.q0
	                - a.q1 * b.q1
	                - a.q2 * b.q2;
	}

	
	/**
	 * Inverts quaternion.
	 * Returns 0 on success.
	 */
	static final int ccdQuatInvert(ccd_quat_t q)
	{
	    double len2 = ccdQuatLen2(q);
	    if (len2 < CCD_EPS)
	        return -1;

	    len2 = CCD_ONE / len2;

	    q.q0 = -q.q0 * len2;
	    q.q1 = -q.q1 * len2;
	    q.q2 = -q.q2 * len2;
	    q.q3 = q.q3 * len2;

	    return 0;
	}
	/**
	 * Inverts quaternion.
	 * Returns 0 on success.
	 */
	public static final int ccdQuatInvert2(ccd_quat_t dest, final ccd_quat_t src)
	{
	    ccdQuatCopy(dest, src);
	    return ccdQuatInvert(dest);
	}



	/**
	 * Rotate vector v by quaternion q.
	 */
	public static final void ccdQuatRotVec(ccd_vec3_t v, final ccd_quat_t q)
	{
	    double w, x, y, z, ww, xx, yy, zz, wx, wy, wz, xy, xz, yz;
	    double vx, vy, vz;

	    w = q.q3;
	    x = q.q0;
	    y = q.q1;
	    z = q.q2;
	    ww = w*w;
	    xx = x*x;
	    yy = y*y;
	    zz = z*z;
	    wx = w*x;
	    wy = w*y;
	    wz = w*z;
	    xy = x*y;
	    xz = x*z;
	    yz = y*z;

	    vx = ww * ccdVec3X(v)
	            + xx * ccdVec3X(v)
	            - yy * ccdVec3X(v)
	            - zz * ccdVec3X(v)
	            + 2 * ((xy - wz) * ccdVec3Y(v)
	            + (xz + wy) * ccdVec3Z(v));
	    vy = ww * ccdVec3Y(v)
	            - xx * ccdVec3Y(v)
	            + yy * ccdVec3Y(v)
	            - zz * ccdVec3Y(v)
	            + 2 * ((xy + wz) * ccdVec3X(v)
	            + (yz - wx) * ccdVec3Z(v));
	    vz = ww * ccdVec3Z(v)
	            - xx * ccdVec3Z(v)
	            - yy * ccdVec3Z(v)
	            + zz * ccdVec3Z(v)
	            + 2 * ((xz - wy) * ccdVec3X(v)
	            + (yz + wx) * ccdVec3Y(v));
	    ccdVec3Set(v, vx, vy, vz);
	}


}
