package org.lhs.notlaos.ui;

/**
 * Meant to allow default screen nub thing on LAOS LCD 
 * to navigate menus so you don't need a mouse
 * @author jediminer543
 *
 */
public interface INubSelect {

	public static enum NubCommand {
		Up,
		Down,
		Left,
		Right,
		Click,
		TL,
		Deselect,
		BL,
		BR
	}
	
	public void HandleCommand(NubCommand nc);
}
