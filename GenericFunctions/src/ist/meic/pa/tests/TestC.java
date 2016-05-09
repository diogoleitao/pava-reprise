package ist.meic.pa.tests;

import ist.meic.pa.GenericFunctions.*;

class Shape {}

class Line extends Shape {}

class Circle extends Shape {}

class Device {}

class Screen extends Device {}

class Printer extends Device {}

public class TestC {

	public static void main(String[] args) {
		
		final GenericFunction draw = new GenericFunction("draw");
		
		draw.addMethod(new GFMethod() {
			Object call(Device d, Shape s) {
				System.err.println("draw what where?");
				return "";
			}});
		
		draw.addMethod(new GFMethod() {
			Object call(Device d, Line l) {
				System.err.println("draw a line where?");
				return "";
			}});
		
		draw.addMethod(new GFMethod() {
			Object call(Device d, Circle c) {
				System.err.println("draw a circle where?");
				return "";
			}});
		
		draw.addMethod(new GFMethod() {
			Object call(Screen d, Shape s) {
				System.err.println("draw what on screen?");
				return "";
			}});
		
		draw.addMethod(new GFMethod() {
			Object call(Screen d, Line l) {
				System.err.println("drawing a line on screen!");
				return "";
			}});
		
		draw.addMethod(new GFMethod() {
			Object call(Screen d, Circle c) {
				System.err.println("drawing a circle on screen!");
				return "";
			}});
		
		draw.addMethod(new GFMethod() {
			Object call(Printer d, Shape s) {
				System.err.println("draw what on printer?");
				return "";
			}});
		
		draw.addMethod(new GFMethod() {
			Object call(Printer d, Line l) {
				System.err.println("drawing a line on printer!");
				return "";
			}});
		
		draw.addMethod(new GFMethod() {
			Object call(Printer d, Circle c) {
				System.err.println("drawing a circle on printer!");
				return "";
			}});

		Device[] devices = new Device[] { new Screen(), new Printer() };
		Shape[] shapes = new Shape[] { new Line(), new Circle() };
		
		for (Device device : devices) {
			for (Shape shape : shapes) {
				draw.call(device, shape);
			}
		}
	}
}
