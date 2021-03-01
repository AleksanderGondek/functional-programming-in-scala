---
title: Notes
---

## General

* Functional data structures - immutability is key characteristic.
* Generic defs: +A means covariant, -A means contravariant. Covariant: param allowed to vary down inheritance tree. Contravariant: parameters vary upward with subtyping. 
* Generic 'A' means invariant - no bouncing inheritance tree, up or down.
* varadic function - can take varying number of arguments
* Case object / class has default implementation for serialization, pattern matchin, enumeration, toString method
* Companion object - object with same name as data type
* match case has no fallover (like switch?)
* Data sharing in functional data structures -> objects are re-used, as there is no risk in doing so