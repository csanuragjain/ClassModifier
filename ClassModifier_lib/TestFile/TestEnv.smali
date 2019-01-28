.class public LTestEnv;
.super Ljava/lang/Object;
.source "TestEnv.java"

.method public constructor <init>()V
  .registers 1
  .prologue
  .line 9
    invoke-direct { p0 }, Ljava/lang/Object;-><init>()V
  .line 11
    return-void
.end method

.method public static main([Ljava/lang/String;)V
  .registers 1
  .prologue
  .line 7
    return-void
.end method

.method public checkEqualOperation(I)V
  .registers 4
  .prologue
  .line 26
    const v0, 9999999
    if-ne p1, v0, :L0
  .line 27
    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;
    const-string v1, "They are equal"
    invoke-virtual { v0, v1 }, Ljava/io/PrintStream;->println(Ljava/lang/String;)V
  :L0
  .line 29
    return-void
.end method

.method public checkNotEqualOperation(I)V
  .registers 4
  .prologue
  .line 32
    const v0, 9999999
    if-eq p1, v0, :L0
  .line 33
    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;
    const-string v1, "They are not equal"
    invoke-virtual { v0, v1 }, Ljava/io/PrintStream;->println(Ljava/lang/String;)V
  :L0
  .line 35
    return-void
.end method

.method public returnHello()Ljava/lang/String;
  .registers 2
  .prologue
  .line 18
    const-string v0, "Hello"
    return-object v0
.end method

.method public returnNumber()I
  .registers 2
  .prologue
  .line 22
    const/4 v0, 5
    return v0
.end method

.method public sayHello()V
  .registers 3
  .prologue
  .line 14
    sget-object v0, Ljava/lang/System;->out:Ljava/io/PrintStream;
    const-string v1, "Hello"
    invoke-virtual { v0, v1 }, Ljava/io/PrintStream;->println(Ljava/lang/String;)V
  .line 15
    return-void
.end method
