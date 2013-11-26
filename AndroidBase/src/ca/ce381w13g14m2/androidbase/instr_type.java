package ca.ce381w13g14m2.androidbase;

/*
 * DoIP Android
 * ============
 * instr_type:  An enumeration used to interpret instructions
 * 				sent and received over the TCP connection
 * 
 * Author:	Keith L
 */
public enum instr_type {
	//Enums
	FILL_PIXEL('P'),
	FILL_SCR('F'),
	SAVE('S'),
	GET_PIXEL('G'),
	GET_W('W'),
	GET_H('H'),
	FILL_COLOR('C'),
	QUIT('X'),
	NONE('0'),
	CONFIRM('N'),
	LINE_START('T'),
	LINE_PT('L'),
	LINE_END('E');
	
	//Holds character associated with enum type
	private final char value;
	
	//Generator
	instr_type(char value)
	{
		this.value=value;
	}
	
	//Gets character associated with type
	public char getValue()
	{
		return this.value;
	}
	
	//Returns instruction type associated with given character
	public static instr_type getType(char value)
	{
		switch(value)
		{
		case 'P':
			return FILL_PIXEL;
		case 'F':
			return FILL_SCR;
		case 'S':
			return SAVE;
		case 'G':
			return GET_PIXEL;
		case 'W':
			return GET_W;
		case 'H':
			return GET_H;
		case 'C':
			return FILL_COLOR;
		case 'X':
			return QUIT;
		case 'N':
			return CONFIRM;
		case 'T':
			return LINE_START;
		case 'L':
			return LINE_PT;
		case 'E':
			return LINE_END;
		case '0':
		default:
			return NONE;
		}
	}
}
