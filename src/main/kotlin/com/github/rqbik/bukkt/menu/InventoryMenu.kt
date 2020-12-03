package com.github.rqbik.bukkt.menu

import com.github.rqbik.bukkt.extensions.event.PluginBoundListener
import com.github.rqbik.bukkt.extensions.event.events
import com.github.rqbik.bukkt.extensions.event.listen
import com.github.rqbik.bukkt.extensions.event.unregister
import com.github.rqbik.bukkt.extensions.inventory.firstEmpty
import com.github.rqbik.bukkt.extensions.item.hideAttributes
import com.github.rqbik.bukkt.extensions.item.item
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.function.Consumer

/**
 * Author: XjCyan1de
 * Modified by: rqibk
 */
open class InventoryMenu
private constructor(
        override val plugin: Plugin,
        val player: Player,
        val inventoryType: InventoryType,
        val rows: Int,
        val title: String,
        private val builder: InventoryMenu.() -> Unit = {}
) : InventoryHolder, PluginBoundListener {
    constructor(plugin: Plugin, player: Player, inventoryType: InventoryType, title: String, builder: InventoryMenu.() -> Unit = {}) : this(plugin, player, inventoryType, 3, title, builder)

    constructor(plugin: Plugin, player: Player, rows: Int, title: String, builder: InventoryMenu.() -> Unit = {}) : this(plugin, player, InventoryType.CHEST, rows, title, builder)

    private val _inventory = if (inventoryType == InventoryType.CHEST)
        Bukkit.createInventory(this, rows * 9, title) else
        Bukkit.createInventory(this, inventoryType, title)

    private val buttons = HashMap<Int, InventoryButton>()

    override fun getInventory(): Inventory = _inventory

    fun addButton(button: InventoryButton): InventoryMenu = addButton(firstEmpty, button)

    fun addButton(material: Material, text: String, clickHandler: InventoryClickEvent.() -> Unit = {}): InventoryMenu =
            addButton(material, text.split("\n"), clickHandler)

    fun addButton(material: Material, vararg text: String, clickHandler: InventoryClickEvent.() -> Unit = {}): InventoryMenu =
            addButton(material, text.toList(), clickHandler)

    fun addButton(material: Material, text: List<String>, clickHandler: InventoryClickEvent.() -> Unit = {}): InventoryMenu {
        val itemStack = createItem(material, text)
        return addButton(itemStack, clickHandler)
    }

    fun addButton(itemStack: ItemStack, clickHandler: InventoryClickEvent.() -> Unit = {}): InventoryMenu =
            addButton(firstEmpty, itemStack, clickHandler)

    @JvmOverloads
    fun addButton(material: Material, text: String, clickHandler: Consumer<InventoryClickEvent> = Consumer {}): InventoryMenu =
            addButton(material, text.split("\n")) { clickHandler.accept(this) }

    @JvmOverloads
    fun addButton(material: Material, vararg text: String, clickHandler: Consumer<InventoryClickEvent> = Consumer {}): InventoryMenu =
            addButton(material, text.toList()) { clickHandler.accept(this) }

    @JvmOverloads
    fun addButton(material: Material, text: List<String>, clickHandler: Consumer<InventoryClickEvent> = Consumer {}): InventoryMenu =
            addButton(material, text) { clickHandler.accept(this) }

    @JvmOverloads
    fun addButton(itemStack: ItemStack, clickHandler: Consumer<InventoryClickEvent> = Consumer {}): InventoryMenu =
            addButton(itemStack) { clickHandler.accept(this) }

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    fun addButton(slot: Int, button: InventoryButton): InventoryMenu = apply {
        buttons[slot] = button
    }

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    fun addButton(slot: Int, material: Material, text: String, clickHandler: InventoryClickEvent.() -> Unit = {}): InventoryMenu =
            addButton(slot, material, text.split("\n"), clickHandler)

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    fun addButton(slot: Int, material: Material, vararg text: String, clickHandler: InventoryClickEvent.() -> Unit = {}): InventoryMenu =
            addButton(slot, material, text.toList(), clickHandler)

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    fun addButton(slot: Int, material: Material, text: List<String>, clickHandler: InventoryClickEvent.() -> Unit = {}): InventoryMenu {
        val itemStack = createItem(material, text)
        return addButton(slot, itemStack, clickHandler)
    }

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    fun addButton(slot: Int, itemStack: ItemStack, clickHandler: InventoryClickEvent.() -> Unit = {}): InventoryMenu = apply {
        val button = InventoryButton(itemStack, clickHandler)
        addButton(slot, button)
    }

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    @JvmOverloads
    fun addButton(slot: Int, material: Material, text: String, clickHandler: Consumer<InventoryClickEvent> = Consumer {}): InventoryMenu =
            addButton(slot, material, text.split("\n")) { clickHandler.accept(this) }

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    @JvmOverloads
    fun addButton(slot: Int, material: Material, vararg text: String, clickHandler: Consumer<InventoryClickEvent> = Consumer {}): InventoryMenu =
            addButton(slot, material, text.toList()) { clickHandler.accept(this) }

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    @JvmOverloads
    fun addButton(slot: Int, material: Material, text: List<String>, clickHandler: Consumer<InventoryClickEvent> = Consumer {}): InventoryMenu =
            addButton(slot, material, text) { clickHandler.accept(this) }

    /**
     * ```
     * |----------------------------|
     * | 0  1  2  3  4  5  6  7  8  |
     * | 9  10 11 12 13 14 15 16 17 |
     * | 18 19 20 21 22 23 24 25 26 |
     * | 27 28 29 30 31 32 33 34 35 |
     * | 36 37 38 39 40 41 42 43 44 |
     * | 45 46 47 48 49 50 51 52 53 |
     * |----------------------------|
     * | 54 55 56 57 58 59 60 61 62 |
     * | 63 64 65 66 67 68 69 70 71 |
     * | 72 73 74 75 76 77 78 79 80 |
     * |----------------------------|
     * | 81 82 83 84 85 86 87 88 89 |
     * |----------------------------|
     * ```
     */
    @JvmOverloads
    fun addButton(slot: Int, itemStack: ItemStack, clickHandler: Consumer<InventoryClickEvent> = Consumer {}): InventoryMenu =
            addButton(slot, itemStack) { clickHandler.accept(this) }

    @JvmOverloads
    fun open(clear: Boolean = true) {
        if (clear) clear()
        builder(this)

        buttons.forEach { (slot, button) ->
            inventory.setItem(slot, button.itemStack)
        }

        player.openInventory(inventory)

        plugin.events(this) {
            listen<InventoryClickEvent> {
                val clickedInventory = clickedInventory ?: return@listen
                if (clickedInventory.holder != this) return@listen

                buttons[slot]?.also {
                    isCancelled = true
                    it.clickHandler(this)
                }
            }

            listen<InventoryCloseEvent> {
                if (inventory.holder != this) return@listen
                onClose(this)

                unregister()
            }
        }
    }

    fun clear() {
        inventory.clear()
        buttons.clear()
    }

    open fun onClose(event: InventoryCloseEvent) {}

    private fun createItem(material: Material, text: List<String>): ItemStack =
        item(material, text = text) {
            hideAttributes()
        }
}

data class InventoryButton(
        val itemStack: ItemStack,
        val clickHandler: InventoryClickEvent.() -> Unit
)

@JvmOverloads
@JvmName("PluginInventoryMenu")
fun Plugin.InventoryMenu(player: Player, inventoryType: InventoryType, title: String, builder: InventoryMenu.() -> Unit = {}): InventoryMenu =
        InventoryMenu(this, player, inventoryType, title, builder)

@JvmOverloads
@JvmName("PluginInventoryMenu")
fun Plugin.InventoryMenu(player: Player, rows: Int, title: String, builder: InventoryMenu.() -> Unit = {}): InventoryMenu =
        InventoryMenu(this, player, rows, title, builder)

@JvmOverloads
fun InventoryMenu.InventoryMenu(inventoryType: InventoryType, title: String, builder: InventoryMenu.() -> Unit = {}): InventoryMenu =
        InventoryMenu(plugin, player, inventoryType, title, builder)

@JvmOverloads
fun InventoryMenu.InventoryMenu(rows: Int, title: String, builder: InventoryMenu.() -> Unit = {}): InventoryMenu =
        InventoryMenu(plugin, player, rows, title, builder)