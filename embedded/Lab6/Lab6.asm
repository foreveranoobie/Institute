#include <p16f84.inc> 

CTR equ 0X2C
START equ 0X10

COPY_CHAR MACRO _CHAR, _ADDR
	movlw _CHAR
	movwf _ADDR	
	endm
	
	ORG   0
	
; 	WR 
	COPY_CHAR 'S', START + .0
	COPY_CHAR 't', START + .1
	COPY_CHAR 'o', START + .2
	COPY_CHAR 'r', START + .3
	COPY_CHAR 'o', START + .4
	COPY_CHAR 'z', START + .5
	COPY_CHAR 'h', START + .6
	COPY_CHAR 'u', START + .7
	COPY_CHAR 'k', START + .8
	
	COPY_CHAR 'O', START + .9
	COPY_CHAR 'l', START + .10
	COPY_CHAR 'e', START + .11
	COPY_CHAR 'k', START + .12
	COPY_CHAR 's', START + .13
	COPY_CHAR 'a', START + .14
	COPY_CHAR 'n', START + .15
	COPY_CHAR 'd', START + .16
	COPY_CHAR 'r', START + .17
	
	COPY_CHAR 'D', START + .18
	COPY_CHAR 'm', START + .19
	COPY_CHAR 'y', START + .20
	COPY_CHAR 't', START + .21
	COPY_CHAR 'r', START + .22
	COPY_CHAR 'o', START + .23
	COPY_CHAR 'v', START + .24
	COPY_CHAR 'i', START + .25
	COPY_CHAR 'c', START + .26
	COPY_CHAR 'h', START + .27

; 	RD
	movlw START
	movwf FSR
	movwf EEADR
	
	movlw .28
	movwf CTR	
@LOOP 
	movf INDF, W
	
;  	WR EEPROM
	movwf EEDATA
	
	bsf STATUS, RP0 		; BANK 1
	bsf EECON1, WREN 		; EN WR
	
	movlw 0X55
	movwf EECON2
	movlw 0XAA
	movwf EECON2
	
	bsf EECON1, WR 			; START WR
	
; 	WAIT WR
	btfsc EECON1, WR
	goto $ - 1
	
	bcf STATUS, RP0 		; BANK 0

	incf FSR
	incf EEADR
	decfsz CTR
	goto @LOOP
	
num EQU 21
	
;	WR
	movlw num
	movwf EEDATA
	
	movlw 0X3F
	movwf EEADR
	
	bsf STATUS, RP0 		; BANK 1
	bsf EECON1, WREN 		; EN WR
	
	movlw 0X55
	movwf EECON2
	movlw 0XAA
	movwf EECON2
	
	bsf EECON1, WR 			; START WR
	
;  	READ EEPROM
	bsf EECON1, RD
		
; 	WAIT WR
	btfsc EECON1, WR
	goto $ - 1	
	
	bcf STATUS, RP0 		; BANK 0
	movf EEDATA, W
	
	END