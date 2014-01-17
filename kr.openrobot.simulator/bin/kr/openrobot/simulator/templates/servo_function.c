

#define PWM	DDRB // #15 Pin. PB4

#define ANGLE_MIN	700 + (8 * (90 - ${devDownAngle}))
#define ANGLE_MID	1500
#define ANGLE_MAX	1500 + (8 * ${devUpAngle})
volatile unsigned int i, j;

void ${devName}_init()
{
	TCCR1A = (1 << COM1A1) | (0 << COM1A0) // Clear OC1A compare match
		| (1 << COM1B1) | (0 << COM1B0)
			| (1 << WGM11) | (0 << WGM10);
	TCCR1B = (1 << WGM13) | (0 << WGM12) // Fast PWM. TOP=OCR1A
			| (0 << CS12) | (1 << CS11) | (0 << CS10); // 1/8 prescaler
	ICR1 = 20000; // 736, Top
	PWM = 0xFF; // OC1A output set.

}

void ${devName}_run()
{
	OCR1A = ANGLE_MIN;
	for(i=0; i<1000; i++)
		for(j=0; j<1000; j++) ;
	OCR1A = ANGLE_MID;
	for(i=0; i<1000; i++)
		for(j=0; j<1000; j++) ;
	OCR1A = ANGLE_MAX;
       	for(i=0; i<1000; i++)
		for(j=0; j<1000; j++) ;
	OCR1A = ANGLE_MID;
	for(i=0; i<1000; i++)
		for(j=0; j<1000; j++) ;
}
