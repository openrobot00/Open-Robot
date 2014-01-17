#include <avr/io.h>
#include <avr/interrupt.h>



${servo_init}

${servo_run}

int main()
{
	cli();
	${devName}_init();
	sei();

	while(1) {

		${devName}_run();

	}
	return 0;
}
