; Auto-generated. Do not edit!


(cl:in-package motor-msg)


;//! \htmlinclude motor.msg.html

(cl:defclass <motor> (roslisp-msg-protocol:ros-message)
  ((angle
    :reader angle
    :initarg :angle
    :type cl:fixnum
    :initform 0)
   (thrust
    :reader thrust
    :initarg :thrust
    :type cl:fixnum
    :initform 0))
)

(cl:defclass motor (<motor>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <motor>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'motor)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name motor-msg:<motor> is deprecated: use motor-msg:motor instead.")))

(cl:ensure-generic-function 'angle-val :lambda-list '(m))
(cl:defmethod angle-val ((m <motor>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader motor-msg:angle-val is deprecated.  Use motor-msg:angle instead.")
  (angle m))

(cl:ensure-generic-function 'thrust-val :lambda-list '(m))
(cl:defmethod thrust-val ((m <motor>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader motor-msg:thrust-val is deprecated.  Use motor-msg:thrust instead.")
  (thrust m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <motor>) ostream)
  "Serializes a message object of type '<motor>"
  (cl:let* ((signed (cl:slot-value msg 'angle)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 65536) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    )
  (cl:let* ((signed (cl:slot-value msg 'thrust)) (unsigned (cl:if (cl:< signed 0) (cl:+ signed 65536) signed)))
    (cl:write-byte (cl:ldb (cl:byte 8 0) unsigned) ostream)
    (cl:write-byte (cl:ldb (cl:byte 8 8) unsigned) ostream)
    )
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <motor>) istream)
  "Deserializes a message object of type '<motor>"
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'angle) (cl:if (cl:< unsigned 32768) unsigned (cl:- unsigned 65536))))
    (cl:let ((unsigned 0))
      (cl:setf (cl:ldb (cl:byte 8 0) unsigned) (cl:read-byte istream))
      (cl:setf (cl:ldb (cl:byte 8 8) unsigned) (cl:read-byte istream))
      (cl:setf (cl:slot-value msg 'thrust) (cl:if (cl:< unsigned 32768) unsigned (cl:- unsigned 65536))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<motor>)))
  "Returns string type for a message object of type '<motor>"
  "motor/motor")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'motor)))
  "Returns string type for a message object of type 'motor"
  "motor/motor")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<motor>)))
  "Returns md5sum for a message object of type '<motor>"
  "ea5f193b4ccf98e6eb37e1ee5739d1ae")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'motor)))
  "Returns md5sum for a message object of type 'motor"
  "ea5f193b4ccf98e6eb37e1ee5739d1ae")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<motor>)))
  "Returns full string definition for message of type '<motor>"
  (cl:format cl:nil "int16 angle~%int16 thrust~%~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'motor)))
  "Returns full string definition for message of type 'motor"
  (cl:format cl:nil "int16 angle~%int16 thrust~%~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <motor>))
  (cl:+ 0
     2
     2
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <motor>))
  "Converts a ROS message object to a list"
  (cl:list 'motor
    (cl:cons ':angle (angle msg))
    (cl:cons ':thrust (thrust msg))
))
