package com.bymarcin.openglasses.testRender;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;

public class Model {
	private ArrayList<Float> buffer = new ArrayList<Float>();
	private PriorityQueue<Matrix> transformation = new PriorityQueue<Matrix>();
	private Matrix color = new Matrix(1, 4);
	private int vertexCount;
	
	public Model() {
		transformation.add(Matrix.generateIdentityMatrix(4));
	}

	public Model(Matrix t) {
		transformation.add(t);
	}

	public void setColor(float r, float g, float b, float alpha){
		color.set(0, 0, r);
		color.set(0, 1, g);
		color.set(0, 2, b);
		color.set(0, 3, alpha);
	}
	
	public void addVertex(float x, float y, float z) {
		Matrix vertex = new Matrix(1, 4);
		vertex.set(0, 0, x);
		vertex.set(0, 1, y);
		vertex.set(0, 2, z);
		vertex.set(0, 3, 1);
		addVertex(Matrix.multiply(transformation.peek(), vertex));
	}
	
	private void addVertex(Matrix vertex){
		for(int i=0;i<color.getHeight(); i++){
			buffer.add(color.get(0, i));
		}
		
		for(int i=0;i<vertex.getHeight()-1; i++){
			buffer.add(vertex.get(0, i));
		}
		vertexCount++;
	}

	public void translate(float x, float y, float z) {
		Matrix translate = Matrix.generateIdentityMatrix(4);
		translate.set(3, 0, x);
		translate.set(3, 1, y);
		translate.set(3, 2, z);
		transformation.add(Matrix.multiply(transformation.poll(), translate));
	}

	public void scale(float x, float y, float z) {
		Matrix scale = Matrix.generateIdentityMatrix(4);
		scale.set(0, 0, x);
		scale.set(1, 1, y);
		scale.set(2, 2, z);
		transformation.add(Matrix.multiply(transformation.poll(), scale));
	}

	public void rotate(float angle, float x, float y, float z) {
		float cosA = (float) Math.toDegrees(Math.cos(Math.toRadians(angle)));
		float sinA = (float) Math.toDegrees(Math.sin(Math.toRadians(angle)));
		if (x != 0) {
			Matrix rotate = Matrix.generateIdentityMatrix(4);
			rotate.set(1, 1, cosA);
			rotate.set(2, 1, -sinA);
			rotate.set(1, 2, sinA);
			rotate.set(2, 2, cosA);
			transformation.add(Matrix.multiply(transformation.poll(), rotate));
		}

		if (y != 0) {
			Matrix rotate = Matrix.generateIdentityMatrix(4);
			rotate.set(0, 0, cosA);
			rotate.set(2, 0, sinA);
			rotate.set(0, 2, -sinA);
			rotate.set(2, 2, cosA);
			transformation.add(Matrix.multiply(transformation.poll(), rotate));
		}

		if (z != 0) {
			Matrix rotate = Matrix.generateIdentityMatrix(4);
			rotate.set(0, 0, cosA);
			rotate.set(0, 1, sinA);
			rotate.set(1, 0, -sinA);
			rotate.set(1, 1, cosA);
			transformation.add(Matrix.multiply(transformation.poll(), rotate));
		}
	}

	public void pushMatrix() {
		transformation.add(transformation.peek());
	}

	public void popMatrix() {
		transformation.poll();
	}
	
	public boolean canCreateShape(){
		return vertexCount%3==0;
	}
	
	public float[] getBuffer(){
		return Floats.toArray(buffer);
	}
	
}
