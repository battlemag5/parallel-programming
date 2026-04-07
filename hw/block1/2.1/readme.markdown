# Task 2.1

## 2.1.a

Is it possible that some exception would happen **inside** `Lock.lock` or `Lock.unlock` method? Justify your answer by using precise `chapter.section` number from Java Language Specification.
The JVM can inject unchecked (especially asynchronous) exceptions at virtually any point (JLS §11.1.3, §11.2.3).
yes
IllegalMonitorStateException 

lock.lock(); <- exception thrown
try {
    // critical section
} finally {
    lock.unlock(); <- never happened => deadlock might occur
}

исключения которые могут быть, как их решить почему некоторые не решить

по поводу задачи про jep, был поставлен вопрос на 2.1(а) — какие варианты эксепшнов могут вылетать в lock/unlock и как с ними можно бороться: из JVM-level Error-ов релевантны StackOverflowError, OutOfMemoryError, AssertionError, InternalError, UnknownError и LinkageError, при этом это не значит, что их нельзя поймать технически, но это именно фатальные состояния или около того, после которых продолжать выполнение небезопасно. По поводу решения, StackOverflow, зная, что написано в jep-e, я вряд-ли предложу решение лучше. JVM резервирует часть стека, чтобы критическая секция успела завершиться и не повредила данные. Для OutOfMemoryError аналогичный подход не подходит: жвм уже не может выделить объект и GC не может освободить достаточно памяти, а значит безопасно добавить память для каждого потока или для каждой критической секции кажется что невозможно, поэтому для ооме, предлагается передавать релевантные флаги: -XX:+HeapDumpOnOutOfMemoryError, -XX:OnOutOfMemoryError=..., -XX:+ExitOnOutOfMemoryError, -XX:+CrashOnOutOfMemoryError. AssertionError в общем случае решается исправлением инварианта или просто отключением ассертов)) InternalError и UnknownError уже уровня жвм сломалась, кажется что их не вылечить обработчиком. LinkageError связаны (их там несоклько) с загрузкой и резолюцией классов, поэтому оно тоже не является задачей для специальной обработки


even if lock() is wrapped with try catch, unlock in this example is going to be illegal if lock was not acquired

**Hint**: `sqrt[3]{1331}` is good magic number.

## 2.1.b

Is it possible to design ''bullet-proof'' (w.r.t. exceptions) concurrency primitives in Java language? Justify your answer by using precise JDK Enhancement Proposal number.

StackOverflowError\OutOfMemoryError (resource-exhaustion errors)
jep 270

Provides a mechanism to mitigate the risk of deadlocks caused by the corruption of critical data such as java.util.concurrent locks (such as ReentrantLock) caused by a StackOverflowError being thrown in a critical section.


Adds reserved stack areas for critical sections Annotated methods (@ReservedStackAccess) can execute even near stack exhaustion which reduces probability of failure in critical sections
Does not ensure correctness under all conditions

A StackOverflowError may still occur at arbitrary points, including inside critical sections.

**Hint**: `sqrt{72900}` is good magic number, too.

## Requirements

Explain **your opinion** on the subject, just citing JLS/JEP is not enough. Be ready to answer additional questions related to sporadic system exceptions in virtual machine.
