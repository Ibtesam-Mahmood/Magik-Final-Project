package com.ibtesam.magik.spellsandeffects;

import com.ibtesam.magik.enumerations.ActionType;
import com.ibtesam.magik.enumerations.ElementType;
import com.ibtesam.magik.enumerations.RuneType;

public class Rune {

	private RuneType type;
	private ElementType eType;
	private ActionType aType;
	
	public Rune(){
		aType = ActionType.NULL;
		eType = ElementType.NULL;
		type = RuneType.NULL;
	}
	
	public Rune(ElementType eType){
		this.eType = eType;
		type = RuneType.ELEMENT;
		aType = ActionType.NULL;
	}
	
	public Rune(ActionType aType){
		this.aType = aType;
		type = RuneType.ACTION;
		eType = ElementType.NULL;
	}

	public RuneType getType() {
		return type;
	}

	public void setType(RuneType type) {
		this.type = type;
	}

	public ElementType geteType() {
		return eType;
	}

	public void seteType(ElementType eType) {
		this.eType = eType;
	}

	public ActionType getaType() {
		return aType;
	}

	public void setaType(ActionType aType) {
		this.aType = aType;
	}
	
	
	
	
	
	
}
