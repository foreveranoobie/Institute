#include <p16f84.inc>

EEPROM_WRITE MACRO

	BSF 	STATUS, RP0 		; BANK 1
	BSF 	EECON1, WREN 		; EN WR
	
	MOVLW 	0X55
	MOVWF 	EECON2
	MOVLW 	0XAA
	MOVWF 	EECON2
	
	BSF 	EECON1, WR 			; START WR	
	BCF 	EECON1, WREN
	BCF 	STATUS, RP0 		; BANK 0
	
	ENDM

TEMP 	EQU 0X0C
N 		EQU 21

RESET_VECTOR
	ORG 	0
	GOTO 	ENTRY_POINT
	
INT_VECTOR
	ORG 	4
	
	BTFSC 	INTCON, INTF
	CALL 	INTRB0
	
	BTFSC 	INTCON, RBIF
	CALL	INTRB47
	
	BTFSC 	INTCON, T0IF
	CALL	INTTMR0
	
	BSF 	STATUS, RP0 		; BANK 1
	BTFSC 	EECON1, EEIF
	CALL	INTEEPROM
	BCF 	STATUS, RP0 		; BANK 0

	RETFIE
	
INTRB0
	MOVLW 	0x00
	TRIS 	PORTB
	
	MOVLW 	N
	MOVWF 	TEMP 		
	MOVWF 	PORTB
	
	MOVLW 	0XFF
	TRIS 	PORTB
	
	BCF 	INTCON, INTF
	RETURN
	
; PORTA = PORTB
INTRB47
	; LO PART
	MOVF 	PORTB, W
	MOVWF 	PORTA
	; HI PART
	MOVWF 	TEMP
	SWAPF 	TEMP, W
	MOVWF  	PORTA
	
	BCF 	INTCON, RBIF
	RETURN
	
INTTMR0
	COMF 	PORTA, F
	MOVLW 	.256-.150
	MOVWF	TMR0
	
	BCF 	INTCON, T0IF
	RETURN

INTEEPROM
	BCF 	STATUS, RP0 		; BANK 0
	MOVLW 	N
	MOVWF 	EEDATA
	INCF 	EEADR, F	
	EEPROM_WRITE
	
	BSF 	STATUS, RP0			; BANK 1
	BCF 	EECON1, EEIF
	BCF 	STATUS, RP0 		; BANK 0
	RETURN
	
ENTRY_POINT
	MOVLW 	0x00
	TRIS    PORTB
	MOVWF 	PORTB
	BSF 	STATUS, RP0			; BANK 1
	MOVLW 	0X10
	MOVWF 	PORTA
	
	MOVLW 	0XFF
	MOVWF 	PORTB
	
	MOVLW 	B'00110001'
	MOVWF 	OPTION_REG	
	BCF 	STATUS, RP0 		; BANK 0
	
	MOVLW 	0X00
	MOVWF 	EEADR
	MOVLW 	0X01
	MOVWF 	EEDATA
	
	EEPROM_WRITE
	
	MOVLW 	0X00
	MOVWF 	PORTA
	MOVLW 	.256-.150
	MOVWF	TMR0
	
	MOVLW 	B'11111000'
	MOVWF 	INTCON
LOOP
	NOP
	GOTO 	LOOP
	
	END