---
title: Notes
---

## Overview

* Higher Order Function - functions that take other functions as arguments and may return function
* REPL - read-evaluate-print-loop
* Module (in Scala) - object which is giving its members namespace
* object.methodName <=> object methodName and object.methodName(beta) <=> object methodName beta
* Importing stuff from modules - import ModuleName.method or _
* Internal, 'nested' helper recursive function is often called 'loop' or 'go' (i.e. loop(n, acc))
* Tail call optimization (TCO) - not optimization in itself; if function returns only recusrive call to itself, will run 'as whileloop'
* One can force compiler to assume function is TCO my using annotation `@annotation.tailrec`
* Monomorprhic function - operates only on a single data type
* Polymorphic function - operates on any type given
* 