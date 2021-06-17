// Auto-generated. Do not edit!

// (in-package motor.msg)


"use strict";

const _serializer = _ros_msg_utils.Serialize;
const _arraySerializer = _serializer.Array;
const _deserializer = _ros_msg_utils.Deserialize;
const _arrayDeserializer = _deserializer.Array;
const _finder = _ros_msg_utils.Find;
const _getByteLength = _ros_msg_utils.getByteLength;

//-----------------------------------------------------------

class motor {
  constructor(initObj={}) {
    if (initObj === null) {
      // initObj === null is a special case for deserialization where we don't initialize fields
      this.angle = null;
      this.thrust = null;
    }
    else {
      if (initObj.hasOwnProperty('angle')) {
        this.angle = initObj.angle
      }
      else {
        this.angle = 0;
      }
      if (initObj.hasOwnProperty('thrust')) {
        this.thrust = initObj.thrust
      }
      else {
        this.thrust = 0;
      }
    }
  }

  static serialize(obj, buffer, bufferOffset) {
    // Serializes a message object of type motor
    // Serialize message field [angle]
    bufferOffset = _serializer.int16(obj.angle, buffer, bufferOffset);
    // Serialize message field [thrust]
    bufferOffset = _serializer.int16(obj.thrust, buffer, bufferOffset);
    return bufferOffset;
  }

  static deserialize(buffer, bufferOffset=[0]) {
    //deserializes a message object of type motor
    let len;
    let data = new motor(null);
    // Deserialize message field [angle]
    data.angle = _deserializer.int16(buffer, bufferOffset);
    // Deserialize message field [thrust]
    data.thrust = _deserializer.int16(buffer, bufferOffset);
    return data;
  }

  static getMessageSize(object) {
    return 4;
  }

  static datatype() {
    // Returns string type for a message object
    return 'motor/motor';
  }

  static md5sum() {
    //Returns md5sum for a message object
    return 'ea5f193b4ccf98e6eb37e1ee5739d1ae';
  }

  static messageDefinition() {
    // Returns full string definition for message
    return `
    int16 angle
    int16 thrust
    
    `;
  }

  static Resolve(msg) {
    // deep-construct a valid message object instance of whatever was passed in
    if (typeof msg !== 'object' || msg === null) {
      msg = {};
    }
    const resolved = new motor(null);
    if (msg.angle !== undefined) {
      resolved.angle = msg.angle;
    }
    else {
      resolved.angle = 0
    }

    if (msg.thrust !== undefined) {
      resolved.thrust = msg.thrust;
    }
    else {
      resolved.thrust = 0
    }

    return resolved;
    }
};

module.exports = motor;
