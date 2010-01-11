import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import javafx.scene.shape.Shape;
import javafx.scene.shape.DelegateShape;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;

import javafx.animation.*;

var shape1 = Rectangle { x: 50, y: 50, width: 100, height: 50 };
var shape2 = Circle { centerX: 50, centerY: 50, radius: 20 };
var geom: Shape;

Stage
{
	title: "Interpolation"
	scene: Scene
	{
		width:  200
		height: 200
		content: DelegateShape
		{
			shape: bind geom
			fill: Color.GREEN
		}
	}
}

var t = Timeline
{
	keyFrames:
	[
		KeyFrame { time: 0s, values: geom => shape1 },
		KeyFrame { time: 5s, values: geom => shape2 tween Interpolator.LINEAR },
	]
};
t.play();
