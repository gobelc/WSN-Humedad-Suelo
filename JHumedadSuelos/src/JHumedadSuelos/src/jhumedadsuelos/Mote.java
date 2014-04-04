package jhumedadsuelos;

// $Id: Mote.java,v 1.2 2008/03/11 11:18:51 a_barbirato Exp $

/*									tab:4
 * Copyright (c) 2007 University College Dublin.
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice and the following
 * two paragraphs appear in all copies of this software.
 *
 * IN NO EVENT SHALL UNIVERSITY COLLEGE DUBLIN BE LIABLE TO ANY
 * PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF 
 * UNIVERSITY COLLEGE DUBLIN HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * UNIVERSITY COLLEGE DUBLIN SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND UNIVERSITY COLLEGE DUBLIN HAS NO
 * OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 * MODIFICATIONS.
 *
 * Authors:	Raja Jurdak, Antonio Ruzzelli, and Samuel Boivineau
 * Date created: 2007/09/07
 *
 */

/**
 * @author Raja Jurdak, Antonio Ruzzelli, and Samuel Boivineau
 */


import java.util.Date;

/*
	This class is a representation of a mote, based
	on the packets received from this mote.
	
	TODO :
			battery support
	BUG :
*/

public class Mote {
	private int moteId;
	private int x, y;
	private boolean anchors;
	private int count, reading, scale;
	private long readingLong, energy;
	private int parentId;
	private int quality; 		// quality of the route to its parent
	private long lastTimeSeen; 	// last time a message was emitted by the mote
	private int battery;
	private boolean sleeping;
	private boolean modeAuto;
	private int samplingPeriod, threshold, sleepDutyCycle, awakeDutyCycle;
	private int channel, conditionInt;
	private long conditionLong;
	private int localize;
	/*
		The constructor lets the user create a new mote by using the fields
		of an automatic message.
	*/
	
	public Mote(int moteId, int count, int reading, long readingLong, int scale, long energy,
				int parentId, int quality, long lastTimeSeen) {
		this.moteId = moteId;
		this.x = (int)(Math.random() * Util.X_MAX);
		this.y = (int)(Math.random() * Util.Y_MAX);
		this.anchors = false;
		this.localize = 0;
		this.count = count;
		this.scale = scale;
		this.reading = reading;
		this.readingLong = readingLong;
		this.energy = energy;
		this.parentId = parentId;
		this.quality = quality;
		this.lastTimeSeen = lastTimeSeen;
		this.battery = Util.UNKNOWN;
		setAwake();
		if(Constants.DEFAULT_MODE == Constants.MODE_AUTO)
			setModeAuto();
		else
			setModeQuery();
		this.samplingPeriod = Constants.DEFAULT_SAMPLING_PERIOD;
		this.threshold = Constants.DEFAULT_THRESHOLD;
		this.sleepDutyCycle = Constants.DEFAULT_SLEEP_DUTY_CYCLE;
		this.awakeDutyCycle = Constants.DEFAULT_AWAKE_DUTY_CYCLE;
	}
	
	public boolean isGateway() {
		if(moteId == 0)
			return true;
		else
			return false;
	}
	public int getMoteId() { return moteId;}

	
	public int getX() { return x;}
	public int getY() { return y;}
	public void setX(int x) { this.x = x;}
	public void setY(int y) { this.y = y;}
	public boolean getAnchors() { return anchors;}
	public void setAnchors(boolean anchors) { this.anchors = anchors;}

	public int getLocalize() { return localize;}
	public void setLocalize(int localize) { this.localize = localize;}
	
	public void setCount(int count) { this.count = count;}
	public int getCount() { return count;}
	public void setReading(int reading) { this.reading = reading;}
	public int getReading() { return reading;}
	public void setReadingLong(long readingLong) { this.readingLong = readingLong;}
	public long getReadingLong() { return readingLong;}
	public void setScale(int scale) { this.scale = scale;}
	public int getScale() { return scale;}
	public void setEnergy(long energy) { this.energy = energy;}
	public long getEnergy() { return energy;}
	public void setParentId(int parentId) { this.parentId = parentId;}
	public int getParentId() { return parentId;}
	public void setQuality(int quality) { this.quality = quality;}
	public int getQuality() { return quality;}
	public void setLastTimeSeen(long lastTimeSeen) { this.lastTimeSeen = lastTimeSeen;}
	public long getLastTimeSeen() { return lastTimeSeen;}
	public long getTimeSinceLastTimeSeen() { 
		Date d = new Date();
		return d.getTime() - lastTimeSeen;
	}
	
	public void setBattery(int battery) { this.battery = battery;}
	public int getBattery() { return battery;}
	public void setSamplingPeriod(int samplingPeriod) { this.samplingPeriod = samplingPeriod;}
	public int getSamplingPeriod() { return samplingPeriod;}
	public void setChannel(int channel){ this.channel = channel;}
	public int getChannel() {return channel;}
	public void setConditionLong(long conditionLong){ this.conditionLong = conditionLong;}
	public long getConditionLong() {return conditionLong;}
	public void setConditionInt(int conditionInt){ this.conditionInt = conditionInt;}
	public int getConditionInt() {return conditionInt;}
	public void setThreshold(int threshold) { this.threshold = threshold;}
	public int getThreshold() { return threshold;}
	public void setSleepDutyCycle(int sleepDutyCycle) { this.sleepDutyCycle = sleepDutyCycle;}
	public int getSleepDutyCycle() { return sleepDutyCycle;}
	public void setAwakeDutyCycle(int awakeDutyCycle) { this.awakeDutyCycle = awakeDutyCycle;}
	public int getAwakeDutyCycle() { return awakeDutyCycle;}
	public boolean isSleeping() { return sleeping;}
	public void setSleeping() { this.sleeping = true;}
	public void setAwake() { this.sleeping = false;}
	public boolean isInModeAuto() { return modeAuto;}
	public void setModeAuto() { this.modeAuto = true;}
	public void setModeQuery() { this.modeAuto = false;}
}
