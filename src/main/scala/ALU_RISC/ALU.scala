package Processing_Unit

import chisel3._
import chisel3.util._

/*
* ALU Instructions          ACTION
* ADD                       Adds the data paths to form data1 + data2
* SUB                       Subtracts the data paths to form data1 - data2
* AND                       Performs a bitwise AND operation on the data paths
* NOT                       Performs a bitwise NOT operation on the data1
* */

class ALU(wordSize: Int, opSize: Int) extends Module{
    val io = IO(new Bundle{
        val data1 = Input(UInt(wordSize.W))
        val data2 = Input(UInt(wordSize.W))
        val opcode = Input(UInt(2.W))
        val ALU_out = Output(UInt(wordSize.W))
        val zFlag = Output(Bool())
    })

    io.zFlag := ~ io.ALU_out.orR

    val tmpRes = Wire(UInt(wordSize.W))
    val tmpZ = Wire(Bool())

    switch(io.opcode){
        is(0.U(2.W)){
            tmpRes := io.data1 + io.data2
            tmpZ := tmpRes === 0.U
        }
        is(1.U(2.W)){
            tmpRes := io.data1 - io.data2
            tmpZ := tmpRes === 0.U
        }
        is(2.U(2.W)){
            tmpRes := io.data1 & io.data2
            tmpZ := tmpRes === 0.U
        }
        is(3.U(2.W)){
            tmpRes := ~io.data1
            tmpZ := tmpRes === 0.U
        }
    }

    io.ALU_out := tmpRes
    io.zFlag := tmpZ
}