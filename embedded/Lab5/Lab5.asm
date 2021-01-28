#include <p16f84.inc>

M equ .93
A equ .25

	ORG 0
	call proc1	; Явний виклик першої функції
	call proc3	; Явний виклик третьої функції
	goto finish
proc1:
	movlw M
	movwf A
	movlw 0xDD 
	movwf PCL	; Занесення адреси другої функції для її виконання
	return
org 0xDD	; Розміщення другої функції за адресою 0xDD
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
    return
org 0x14B
proc3:
	movlw b'00110111'
	option
Loop    bcf PORTB, 5
	bcf INTCON, T0IF
	movlw .246
	movwf TMR0
m1	btfss INTCON, T0IF
	goto m1
	bsf PORTB, 5
	return
finish
	bcf PORTB, 1
END