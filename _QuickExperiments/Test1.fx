import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import javafx.scene.shape.*;

import javafx.scene.input.MouseEvent;

import javafx.animation.*;

var elements: PathElement[];

Stage
{
	title: "Simple Painter"
	scene: Scene
	{
		content:
		[
			Rectangle
			{
				width: 400
				height: 300
				fill: Color.WHITE

				onMousePressed: function(e: MouseEvent): Void
				{
					insert MoveTo { x: e.x, y: e.y } into elements;
				}
				onMouseDragged: function(e: MouseEvent): Void
				{
					insert LineTo { x: e.x, y: e.y } into elements;
				}
			}
			Path
			{
				stroke: Color.BLUE
				elements: bind elements
			}
		]
	}
}
