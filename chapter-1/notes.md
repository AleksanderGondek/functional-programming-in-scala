---
title: Notes
---

## Overview

* Side effect in function - anything that function may do besides returning a value (modifying state - externally, internally, whatever)
* Function implies pure function, procedure implies function with side-effect(s)
* Pure function - function that does not have any side-effects. A => B, always produces the same output, for the same input, without making any other changes
* Referential transparency - expression may be replaced by its result without changing the meaning of a program. Function is pure if calling it with referential transparent inputs
* Substitution model - if all expressions in program are referential transparent, then we can reason about it as with algebra equations
