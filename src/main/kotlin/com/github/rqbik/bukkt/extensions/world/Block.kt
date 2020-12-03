package com.github.rqbik.bukkt.extensions.world

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.generator.ChunkGenerator
import org.bukkit.util.BlockVector

inline val Long.blockKeyX: Int get() = Block.getBlockKeyX(this)
inline val Long.blockKeyY: Int get() = Block.getBlockKeyY(this)
inline val Long.blockKeyZ: Int get() = Block.getBlockKeyZ(this)

fun BlockVector(long: Long): BlockVector = BlockVector(Block.getBlockKeyX(long), Block.getBlockKeyY(long), Block.getBlockKeyZ(long))
fun BlockVector.toBlockKey(): Long = Block.getBlockKey(blockX, blockY, blockZ)
fun Location.toBlockVector(): BlockVector = BlockVector(blockX, blockY, blockZ)

operator fun ChunkGenerator.ChunkData.set(x: Int, y: Int, z: Int, blockData: BlockData) = setBlock(x, y, z, blockData)
operator fun ChunkGenerator.ChunkData.set(x: Int, y: Int, z: Int, material: Material) = setBlock(x, y, z, material)

fun Block.sendBlockChange(
    material: Material,
    players: List<Player>
) = sendBlockChange(material.createBlockData(), players)

fun Block.sendBlockChange(
    blockData: BlockData,
    players: List<Player>
) {
    players.filter { it.world.name == world.name }.forEach {
        it.sendBlockChange(location, blockData)
    }
}

operator fun Block.component1() = x
operator fun Block.component2() = y
operator fun Block.component3() = z