////////////////////////starting arm1.cpp////////////////////////
//  sample13.cpp  3 DOF manipulator by   Kosei Demura  2006-2009
// I just modified above code from the www.demura.net
// Dynamics and collision detection is implemented
// I'll change control part by myself, it'll be different from the original code

// Control is implemented and I'v inserted the part that calculating the length and COM point of each arm by their lengh
// So u can change the length and radius of the each arm, and there'll be no problem related with the collision.
#include <ode/ode.h>
#include <drawstuff/drawstuff.h>
#define NUM 3

#define DRAWSTUFF_TEXTURE_PATH "textures"

#ifdef test
////////test
dBodyID testBody;
dGeomID testGeom;
dMass mass2;
/////////
#endif

int deg1;
int deg2;
int spd;

//collide variables
dSpaceID space;
dGeomID linkGeom[NUM];
dGeomID groundGeom;
dJointGroupID contactJointGroup;

//dynamic variables
dWorldID world;
dBodyID link[NUM];//link body
dJointID joint[NUM];//joint connecting each links
static double THETA[NUM] = { M_PI/4, M_PI/4, M_PI/4 /*M_PI/4*/ }; //target angle

//initial location of the bottom arm
static double initX = 0.00;//my
static double initY = 0.00;//my
static double initZ = 0.00;//my

//arm's lengh and radius
static double l[NUM] = { 0.10, 0.50, 0.50/*, 0.50*/ };//lengh of link
static double r[NUM] = { 0.20, 0.04, 0.04/*, 0.04*/ };//radius of link

static void nearCallback(void *data, dGeomID o1, dGeomID o2)
{
	dBodyID b1 = dGeomGetBody(o1);
	dBodyID b2 = dGeomGetBody(o2);

	if(b1 && b2 && dAreConnectedExcluding(b1, b2, dJointTypeContact)) return;

	bool condGroundContact = ( (o1 == groundGeom ) || (o2 == groundGeom ) );
	bool condObjectContact = ( (o1 != groundGeom ) && (o2 != groundGeom ) );

	
	if( condGroundContact )
	{
		static const int N = 10;
		dContact contact[N];
		int n = dCollide(o1, o2, N, &contact[0].geom, sizeof(dContact));
		if(n>0)
		{
			for(int i=0; i<n; i++)
			{
				contact[i].surface.mode = dContactSoftERP | dContactSoftCFM | dContactBounce;
				contact[i].surface.mu = dInfinity;
				contact[i].surface.soft_erp = 0.5;
				contact[i].surface.soft_cfm = 1e-4;
				contact[i].surface.bounce = 0.5;
				dJointID c = dJointCreateContact(world, contactJointGroup, &contact[i]);
				dJointAttach(c, b1, b2);
			}
		}
	}
	else if( condObjectContact )
	{
		static const int N = 10;
		dContact contact[N];
		int n = dCollide(o1, o2, N, &contact[0].geom, sizeof(dContact));
		if(n>0)
		{
			for(int i=0; i<n; i++)
			{
				contact[i].surface.mode = dContactSoftERP | dContactSoftCFM | dContactBounce;
				contact[i].surface.mu = dInfinity;
				contact[i].surface.soft_erp = 0.5;
				contact[i].surface.soft_cfm = 1e-4;
				contact[i].surface.bounce = 0.5;
				dJointID c = dJointCreateContact(world, contactJointGroup, &contact[i]);
				dJointAttach(c, b1, b2);
			}
		}
	}
}
 
void control()
{
	double k1 = 10, fMax = 100.0;


	for( int j=1; j<NUM; j++ )
	{
		double tmpAngle = dJointGetHingeAngle(joint[j]);
		double z = THETA[j] - tmpAngle;
		dJointSetHingeParam(joint[j], dParamVel, k1*z);
		dJointSetHingeParam(joint[j], dParamFMax, fMax);
	}
}

void start(void)/* called before sim loop starts */
{
	float xyz[3] = {3.04, 1.28, 0.76};
	float hpr[3] = {-160.0, 4.50, 0.00};
	dsSetViewpoint(xyz, hpr);
}		

void step(int pause)/* called before every frame */
{
	control();
	dSpaceCollide(space, 0, &nearCallback);
	dWorldStep(world, 0.01);
	dJointGroupEmpty(contactJointGroup);

	//drawing object
	for( int i=0; i < NUM; i++ )
	{
		dsDrawCapsule( dBodyGetPosition(link[i]), dBodyGetRotation(link[i]), l[i], r[i] );
	}
	//dsDrawSphere( dBodyGetPosition(testBody), dBodyGetRotation(testBody), 0.2);
}

void command(int cmd)/* called if a command key is pressed */
{	
	switch(cmd)
	{
	case 'q':
	case 'Q':
		dsStop();
		break;

/*	case 'x':
		THETA[1] += 0.05;
		break;

	case 'c':
		THETA[1] -= 0.05;
		break;
*/
	case 's':
		THETA[2] += (M_PI/180)*spd;
		break;

	case 'd':
		THETA[2] -= (M_PI/180)*spd;
		break;
/*
	case 'w':
		THETA[3] += 0.05;
		break;

	case 'e':
		THETA[3] -= 0.05;
		break;
*/

	default:
		break;
	}

  if (THETA[1] <   - M_PI)   THETA[1] =  - M_PI;   if (THETA[1] >     M_PI)    THETA[1] =    M_PI;
  if (THETA[2] <  (M_PI/180)*deg1)  THETA[2] =  (M_PI/180)*deg1;
  if (THETA[2] >  (M_PI/180)*deg2)  THETA[2] =   (M_PI/180)*deg2;//2*M_PI/7;
  if (THETA[3] <  -2*M_PI/3)  THETA[3] =  - 2*M_PI/3;
  if (THETA[3] >   2*M_PI/3)  THETA[3] =   2*M_PI/3;


}

void stop(void)
{
}



int main( int argc, char* argv[] )
{
		dsFunctions fn;
		fn.version = DS_VERSION;
		fn.start = start;
		fn.step = step;
		fn.command = command;
		fn.stop = NULL;
		fn.path_to_textures = DRAWSTUFF_TEXTURE_PATH;
	

		double x[NUM] = {0.00};
		double y[NUM] = {0.00};
		//double z[NUM] = {0.05, 0.50, 1.50, 2.55};
		double z[NUM] = {0.00};//my
		//setting each links COM
		for( int i=0; i<NUM; i++ )//my
		{	
			if( i==0 ) 
				z[i] = initZ + l[i]/2 + r[i];
			else
				z[i] = z[i-1] + l[i-1]/2 + l[i]/2 + r[i-1] + r[i];
		}



		double m[NUM] = {10.00, 2.00, 2.00};//, 2.00};
		
		double anchor_x[NUM] = {0.00};
		double anchor_y[NUM] = {0.00};
		//double anchor_z[NUM] = {0.00, 0.10, 1.00, 2.00};
		double anchor_z[NUM] = {0.00};//my
		for( int i=0; i<NUM; i++ )//my
		{
			if( i==0 )
				anchor_z[i] = z[i-1] - l[i-1]/2 - r[i-1]/2;
			else
				anchor_z[i] = z[i-1] + l[i-1]/2 + r[i-1]/2;
		}


		double axis_x[NUM] = {0.00, 0.00, 0.00};//, 0.00};
		double axis_y[NUM] = {0.00, 0.00, 1.00};//, 1.00};
		double axis_z[NUM] = {1.00, 1.00, 0.00};//, 0.00};
		

		dInitODE();
		//ODE command start
		world = dWorldCreate();//world for dynamic
		space = dHashSpaceCreate(0);//space for collision
		groundGeom = dCreatePlane(space, 0, 0, 1, 0);//geom for ground(ax+by+cz=d)
		dWorldSetGravity(world, 0, 0, -9.8);//setting gravity
		contactJointGroup = dJointGroupCreate(0);//joint group for collision

		//setting body and their mass
		for( int i = 0; i < NUM; i++ )
		{
			dMass mass;
			link[i] = dBodyCreate(world);
			dBodySetPosition( link[i], x[i], y[i], z[i] );
			dMassSetZero(&mass);
			dMassSetCapsuleTotal(&mass, m[i], 3, r[i], l[i]);
			dBodySetMass(link[i], &mass);
		}

		
		//setting joints
		joint[0] = dJointCreateFixed(world, 0);
		dJointAttach(joint[0], link[0], 0);
		dJointSetFixed(joint[0]);
		for( int j=1; j<NUM; j++)
		{
			joint[j] = dJointCreateHinge(world, 0);
			dJointAttach(joint[j], link[j-1], link[j]);
			dJointSetHingeAnchor(joint[j], anchor_x[j], anchor_y[j], anchor_z[j]);
			dJointSetHingeAxis(joint[j], axis_x[j], axis_y[j], axis_z[j]);
			//dJointSetHingeParam(joint[j], dParamSuspensionERP, 1);
		}
		
		for( int j=0; j<NUM; j++)
		{
			linkGeom[j] = dCreateCapsule(space, r[j], l[j]);
			dGeomSetBody(linkGeom[j], link[j]);
		}
// up angle
deg1 = atoi(argv[1]);
// down angle
deg2 = atoi(argv[2]);
// spd
spd = atoi(argv[3]);
// loop
//if (argv[4] == 'true')

#ifdef test
		/////////test
		testBody = dBodyCreate(world);
		dMassSetZero(&mass2);
		dMassSetSphereTotal(&mass2, 1, 0.2);
		dBodySetMass(testBody, &mass2);
		dBodySetPosition(testBody, 0, -1, 2);
		testGeom = dCreateSphere(space, 0.2);
		dGeomSetBody( testGeom, testBody);
		//////
#endif

		//ODE command end
		dsSimulationLoop( argc, argv, 600, 600, &fn );
		dCloseODE();
		return 0;
}
