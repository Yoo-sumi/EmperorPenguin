;; Auto-generated. Do not edit!


(when (boundp 'motor::motor)
  (if (not (find-package "MOTOR"))
    (make-package "MOTOR"))
  (shadow 'motor (find-package "MOTOR")))
(unless (find-package "MOTOR::MOTOR")
  (make-package "MOTOR::MOTOR"))

(in-package "ROS")
;;//! \htmlinclude motor.msg.html


(defclass motor::motor
  :super ros::object
  :slots (_angle _thrust ))

(defmethod motor::motor
  (:init
   (&key
    ((:angle __angle) 0)
    ((:thrust __thrust) 0)
    )
   (send-super :init)
   (setq _angle (round __angle))
   (setq _thrust (round __thrust))
   self)
  (:angle
   (&optional __angle)
   (if __angle (setq _angle __angle)) _angle)
  (:thrust
   (&optional __thrust)
   (if __thrust (setq _thrust __thrust)) _thrust)
  (:serialization-length
   ()
   (+
    ;; int16 _angle
    2
    ;; int16 _thrust
    2
    ))
  (:serialize
   (&optional strm)
   (let ((s (if strm strm
              (make-string-output-stream (send self :serialization-length)))))
     ;; int16 _angle
       (write-word _angle s)
     ;; int16 _thrust
       (write-word _thrust s)
     ;;
     (if (null strm) (get-output-stream-string s))))
  (:deserialize
   (buf &optional (ptr- 0))
   ;; int16 _angle
     (setq _angle (sys::peek buf ptr- :short)) (incf ptr- 2)
   ;; int16 _thrust
     (setq _thrust (sys::peek buf ptr- :short)) (incf ptr- 2)
   ;;
   self)
  )

(setf (get motor::motor :md5sum-) "ea5f193b4ccf98e6eb37e1ee5739d1ae")
(setf (get motor::motor :datatype-) "motor/motor")
(setf (get motor::motor :definition-)
      "int16 angle
int16 thrust

")



(provide :motor/motor "ea5f193b4ccf98e6eb37e1ee5739d1ae")


