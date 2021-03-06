/*************************************************************************
 *                                                                       *
 * Open Dynamics Engine, Copyright (C) 2001,2002 Russell L. Smith.       *
 * All rights reserved.  Email: russ@q12.org   Web: www.q12.org          *
 * Open Dynamics Engine 4J, Copyright (C) 2007-2010 Tilmann Zäschke      *
 * All rights reserved.  Email: ode4j@gmx.de   Web: www.ode4j.org        *
 *                                                                       *
 * This library is free software; you can redistribute it and/or         *
 * modify it under the terms of EITHER:                                  *
 *   (1) The GNU Lesser General Public License as published by the Free  *
 *       Software Foundation; either version 2.1 of the License, or (at  *
 *       your option) any later version. The text of the GNU Lesser      *
 *       General Public License is included with this library in the     *
 *       file LICENSE.TXT.                                               *
 *   (2) The BSD-style license that is included with this library in     *
 *       the file ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT.         *
 *                                                                       *
 * This library is distributed in the hope that it will be useful,       *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the files    *
 * LICENSE.TXT, ODE-LICENSE-BSD.TXT and ODE4J-LICENSE-BSD.TXT for more   *
 * details.                                                              *
 *                                                                       *
 *************************************************************************/
package org.ode4j.ode.internal.joints;

import static org.ode4j.ode.OdeConstants.dInfinity;
import static org.ode4j.ode.OdeMath.dCalcVectorCross3;

import org.ode4j.math.DVector3;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DJoint.PARAM;
import org.ode4j.ode.internal.DxWorld;
import org.ode4j.ode.internal.joints.DxJoint.Info2;

/**
 * common limit and motor information for a single joint axis of movement
 *
 */
public class DxJointLimitMotor {
	public double vel;        // powered joint: velocity, max force
	public double fmax;
	public double lostop;   // joint limits, relative to initial position
	public double histop;
	public double fudge_factor;     // when powering away from joint limits
	public double normal_cfm;       // cfm to use when not at a stop
	public double stop_erp; // erp and cfm for when at joint limit
	double stop_cfm;
	public double bounce;           // restitution factor
	// variables used between getInfo1() and getInfo2()
	public int limit;          // 0=free, 1=at lo limit, 2=at hi limit
	double limit_err;    // if at limit, amount over limit

	//****************************************************************************
	// dxJointLimitMotor


	public void init( DxWorld world )
	{
		vel = 0;
		fmax = 0;
		lostop = -dInfinity;
		histop = dInfinity;
		fudge_factor = 1;
		normal_cfm = world.getCFM();
		stop_erp = world.getERP();
		stop_cfm = world.getCFM();
		bounce = 0;
		limit = 0;
		limit_err = 0;
	}


	public void set( PARAM num, double value )
	{
		switch ( num )
		{
		case dParamLoStop:
			lostop = value;
			break;
		case dParamHiStop:
			histop = value;
			break;
		case dParamVel:
			vel = value;
			break;
		case dParamFMax:
			if ( value >= 0 ) fmax = value;
			break;
		case dParamFudgeFactor:
			if ( value >= 0 && value <= 1 ) fudge_factor = value;
			break;
		case dParamBounce:
			bounce = value;
			break;
		case dParamCFM:
			normal_cfm = value;
			break;
		case dParamStopERP:
			stop_erp = value;
			break;
		case dParamStopCFM:
			stop_cfm = value;
			break;
			default:
				throw new IllegalArgumentException(num.name());
		}
	}


	public double get( PARAM num )
	{
		switch ( num )
		{
		case dParamLoStop:
			return lostop;
		case dParamHiStop:
			return histop;
		case dParamVel:
			return vel;
		case dParamFMax:
			return fmax;
		case dParamFudgeFactor:
			return fudge_factor;
		case dParamBounce:
			return bounce;
		case dParamCFM:
			return normal_cfm;
		case dParamStopERP:
			return stop_erp;
		case dParamStopCFM:
			return stop_cfm;
		default:
			//return 0;
			throw new IllegalArgumentException("" + num.name());
		}
	}


	public boolean testRotationalLimit( double angle )
	{
		if ( angle <= lostop )
		{
			limit = 1;
			limit_err = angle - lostop;
			return true;
		}
		else if ( angle >= histop )
		{
			limit = 2;
			limit_err = angle - histop;
			return true;
		}
		else
		{
			limit = 0;
			return false;
		}
	}


	// public  int addLimot( dxJoint joint,
			//         Info2 info, int row,
			//         final dVector3 ax1, int rotational )
	public  int addLimot( DxJoint joint,
			Info2 info, int row,
			final DVector3C ax1, boolean rotational )
	{
		int srow = row * info.rowskip();

		// if the joint is powered, or has joint limits, add in the extra row
		boolean powered = fmax > 0;
		if ( powered || limit != 0)
		{
//			dMatrix3 J1 = rotational ? info.J1a : info.J1l;
//			dMatrix3 J2 = rotational ? info.J2a : info.J2l;
//
//			J1.v[srow+0] = ax1.v[0];
//			J1.v[srow+1] = ax1.v[1];
//			J1.v[srow+2] = ax1.v[2];
//			if ( joint.node[1].body != null )
//			{
//				J2.v[srow+0] = -ax1.v[0];
//				J2.v[srow+1] = -ax1.v[1];
//				J2.v[srow+2] = -ax1.v[2];
//			}
			if (rotational) {
				info._J[info.J1ap+srow+0] = ax1.get0();
				info._J[info.J1ap+srow+1] = ax1.get1();
				info._J[info.J1ap+srow+2] = ax1.get2();
				if ( joint.node[1].body != null )
				{
					info._J[info.J2ap+srow+0] = -ax1.get0();
					info._J[info.J2ap+srow+1] = -ax1.get1();
					info._J[info.J2ap+srow+2] = -ax1.get2();
				}
			} else {
				info._J[info.J1lp+srow+0] = ax1.get0();
				info._J[info.J1lp+srow+1] = ax1.get1();
				info._J[info.J1lp+srow+2] = ax1.get2();
				if ( joint.node[1].body != null )
				{
					info._J[info.J2lp+srow+0] = -ax1.get0();
					info._J[info.J2lp+srow+1] = -ax1.get1();
					info._J[info.J2lp+srow+2] = -ax1.get2();
				}
			}


			// linear limot torque decoupling step:
				//
				// if this is a linear limot (e.g. from a slider), we have to be careful
			// that the linear constraint forces (+/- ax1) applied to the two bodies
			// do not create a torque couple. in other words, the points that the
			// constraint force is applied at must lie along the same ax1 axis.
			// a torque couple will result in powered or limited slider-jointed free
			// bodies from gaining angular momentum.
			// the solution used here is to apply the constraint forces at the point
			// halfway between the body centers. there is no penalty (other than an
			// extra tiny bit of computation) in doing this adjustment. note that we
			// only need to do this if the constraint connects two bodies.

			DVector3 ltd = new DVector3(0, 0, 0);//{0,0,0}); // Linear Torque Decoupling vector (a torque)
			if ( (!rotational) && (joint.node[1].body != null))
			{
				DVector3 c = new DVector3();
//				c.v[0] = 0.5 * ( joint.node[1].body._posr.pos.v[0] - joint.node[0].body._posr.pos.v[0] );
//				c.v[1] = 0.5 * ( joint.node[1].body._posr.pos.v[1] - joint.node[0].body._posr.pos.v[1] );
//				c.v[2] = 0.5 * ( joint.node[1].body._posr.pos.v[2] - joint.node[0].body._posr.pos.v[2] );
				c.eqDiff(joint.node[1].body.posr().pos(), joint.node[0].body.posr().pos()).scale(0.5);
				dCalcVectorCross3( ltd, c, ax1 );
				info._J[info.J1ap+srow+0] = ltd.get0();
				info._J[info.J1ap+srow+1] = ltd.get1();
				info._J[info.J1ap+srow+2] = ltd.get2();
				info._J[info.J2ap+srow+0] = ltd.get0();
				info._J[info.J2ap+srow+1] = ltd.get1();
				info._J[info.J2ap+srow+2] = ltd.get2();
			}

			// if we're limited low and high simultaneously, the joint motor is
			// ineffective
			if ( limit!=0 && ( lostop == histop ) ) powered = false;

			if ( powered )
			{
				info.setCfm(row, normal_cfm);
				if ( limit == 0 )
				{
					info.setC(row, vel);
					info.setLo(row, -fmax);
					info.setHi(row, fmax);
				}
				else
				{
					// the joint is at a limit, AND is being powered. if the joint is
					// being powered into the limit then we apply the maximum motor force
					// in that direction, because the motor is working against the
					// immovable limit. if the joint is being powered away from the limit
					// then we have problems because actually we need *two* lcp
					// constraints to handle this case. so we fake it and apply some
					// fraction of the maximum force. the fraction to use can be set as
					// a fudge factor.

					double fm = fmax;
					if (( vel > 0 ) || ( vel == 0 && limit == 2 ) ) fm = -fm;

					// if we're powering away from the limit, apply the fudge factor
					if (( limit == 1 && vel > 0 ) || ( limit == 2 && vel < 0 ) ) fm *= fudge_factor;

					if ( rotational )
					{
						joint.node[0].body.dBodyAddTorque( -fm*ax1.get0(), -fm*ax1.get1(),
								-fm*ax1.get2() );
						if ( joint.node[1].body != null)
							joint.node[1].body.dBodyAddTorque( fm*ax1.get0(), fm*ax1.get1(), fm*ax1.get2() );
					}
					else
					{
						joint.node[0].body.dBodyAddForce( -fm*ax1.get0(), -fm*ax1.get1(), -fm*ax1.get2() );
						if ( joint.node[1].body != null)
						{
							joint.node[1].body.dBodyAddForce( fm*ax1.get0(), fm*ax1.get1(), fm*ax1.get2() );

							// linear limot torque decoupling step: refer to above discussion
							joint.node[0].body.dBodyAddTorque( -fm*ltd.get0(), -fm*ltd.get1(),
									-fm*ltd.get2() );
							joint.node[1].body.dBodyAddTorque( -fm*ltd.get0(), -fm*ltd.get1(),
									-fm*ltd.get2() );
						}
					}
				}
			}

			if ( limit != 0 )
			{
				double k = info.fps * stop_erp;
				info.setC(row, -k * limit_err);
				info.setCfm(row, stop_cfm);

				if ( lostop == histop )
				{
					// limited low and high simultaneously
					info.setLo(row, -dInfinity);
					info.setHi(row, dInfinity);
				}
				else
				{
					if ( limit == 1 )
					{
						// low limit
						info.setLo(row, 0);
						info.setHi(row, dInfinity);
					}
					else
					{
						// high limit
						info.setLo(row, -dInfinity);
						info.setHi(row, 0);
					}

					// deal with bounce
					if ( bounce > 0 )
					{
						// calculate joint velocity
						double vel;
						if ( rotational )
						{
							vel = joint.node[0].body.avel.dot( ax1 );
							if ( joint.node[1].body != null)
								vel -= joint.node[1].body.avel.dot( ax1 );
						}
						else
						{
							vel = joint.node[0].body.lvel.dot( ax1 );
							if ( joint.node[1].body != null)
								vel -= joint.node[1].body.lvel.dot( ax1 );
						}

						// only apply bounce if the velocity is incoming, and if the
						// resulting c[] exceeds what we already have.
						if ( limit == 1 )
						{
							// low limit
							if ( vel < 0 )
							{
								double newc = -bounce * vel;
								if ( newc > info.getC(row) ) info.setC(row, newc);
							}
						}
						else
						{
							// high limit - all those computations are reversed
							if ( vel > 0 )
							{
								double newc = -bounce * vel;
								if ( newc < info.getC(row) ) info.setC(row, newc);
							}
						}
					}
				}
			}
			return 1;
		}
		else return 0;
	}

	//************** TZ stuff  ****************
	
	
	public int getLimit() {
		return limit;
	}


	public void setLimit(int i) {
		limit = i;
	}


	public double getLostop() {
		return lostop;
	}


}
