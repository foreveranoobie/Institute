#include <p16f84.inc>

A equ .25

	ORG 0
	movlw	0
	tris	PORTA
	movlw	0xFF
	tris	PORTB
;основна програма
	movf	PORTB, 0
	movwf	A
	movwf	PORTA
	swapf	A, 0
	movwf	PORTA
loop	goto	loop
	END