/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider;

import com.SatanicSpider.Main;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
//import javafx.scene.control.ButtonBuilder;

/**
 *
 * @author bryant
 */
public class ScriptConsole extends SimpleApplication
{

	boolean run = true;
	static Main app;
	static ScriptConsole script;

	public static void main(String[] args)
	{
		//app = new Main();
		script = new ScriptConsole();
		//app.setDisplayFps(false);
		//app.setDisplayStatView(false);
		//app.start();
		script.start();
		
	}
	
	public void simpleInitApp()
	{
		flyCam.setDragToRotate(true);
		NiftyJmeDisplay nd = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
		
		Nifty n = nd.getNifty();
		guiViewPort.addProcessor(nd);
		n.loadStyleFile("nifty-default-styles.xml");
		n.loadControlFile("nifty-default-controls.xml");
		n.addScreen("main", new ScreenBuilder("Main"){{
			controller(new DefaultScreenController());
			layer(new LayerBuilder("top") {{
				childLayoutVertical();
				panel(new PanelBuilder("top") {{
					childLayoutCenter();
					alignCenter();
					backgroundColor("#f008");
					height("45%");
					width("75%");
					control(new TextFieldBuilder("Text","Text") {{
						width("75%");
						height("75%");
						
					}});
				}});
				panel(new PanelBuilder("mid") {{
					childLayoutCenter();
					alignCenter();
					backgroundColor("#0f08");
					height("10%");
					width("75%");
					panel(new PanelBuilder("midleft") {{
						childLayoutCenter();
						//alignCenter();
						alignLeft();
						backgroundColor("#f002");
						height("100%");
						width("50%");
						control(new ButtonBuilder("Save","Save") {{
							alignLeft();
							valignCenter();
							width("45%");
							height("75%");
						}});
						control(new ButtonBuilder("Load","Load") {{
							alignRight();
							valignCenter();
							width("45%");
							height("75%");
						}});
					}});
					panel(new PanelBuilder("midright") {{
						childLayoutCenter();
						//alignCenter();
						alignRight();
						backgroundColor("#f006");
						height("100%");
						width("50%");
						control(new ButtonBuilder("Stop","Stop") {{
							alignLeft();
							valignCenter();
							width("27%");
							height("75%");
						}});
						control(new ButtonBuilder("Pause","Pause") {{
							//alignRight();
							valignCenter();
							width("27%");
							height("75%");
						}});
						control(new ButtonBuilder("Start","Start") {{
							alignRight();
							valignCenter();
							width("27%");
							height("75%");
						}});
					}});
				}});
				
				panel(new PanelBuilder("bot") {{
					childLayoutCenter();
					alignCenter();
					backgroundColor("#00f8");
					height("45%");
					width("75%");
					
				}});
			}});
		}}.build(n));
		n.gotoScreen("main");
	}
	
}
