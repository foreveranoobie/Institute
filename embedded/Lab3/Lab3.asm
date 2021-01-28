#include <p16f84.inc>

	ORG 0
	movlw b'00110111'
	option
Loop    bcf PORTB, 5
	bcf INTCON, T0IF
	movlw .246
	movwf TMR0
m1	btfss INTCON, T0IF
	goto m1
	bsf PORTB, 5
	goto Loop
END