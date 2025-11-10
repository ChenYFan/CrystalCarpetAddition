## Crystal Carpet Addition CyanEdition

> [!NOTE]
> 
> 这是一个CyanFalse修改的Crystal Carpet Addition模组，用于自己的Nyirusu Minecraft Server系列。


## 已做的修改如下

### `GatewayCannotLoadingChunks` 规则

类型：~`Boolean`~ `String`

可选值: `None`, `All`, `Minecraft允许的Entity Type（例如Player、Item、Villager）`

默认值: `None` （允许所有实体创建加载票）

> 在24w21a，Mojang引入了`实体穿越折跃门时会在目标区块创建加载票`的功能。
>
> 这***意外的***（？）导致了虚空交易被彻底削弱。如果尝试在该版本中虚空交易（启用RemoveVillagerTradeDistanceLimit规则），在第二次离开村民所在区块时，若距离第一次返回事件不超过15s，会导致村民所在区块会在实际上被加载，导致虚空交易失效。
>
> CCA原本的规则是`GatewayCannotLoadingChunks`，可以阻止实体穿越折跃门时创建加载票。
>
> 但这条规则是一刀切的，会导致折跃门加载器无法使用。
>
> 因此我将这条规则改为`String`类型，可以指定阻止哪些实体创建加载票。
>
> 例如，设置为`Player`，可以在玩家通过时阻止折跃门创建加载票（PS：当然这不能避免Player加载票 PSS：但是Player加载票在Player离开时就会销毁，不会多赖15s）
>
> 设置为`Item`，可以在物品通过时阻止折跃门创建加载票
>
> 设置为`None`，则允许所有实体创建加载票（和原本关闭该规则效果相同）
>
> 设置为`All`，则和原本的Boolean规则效果相同，阻止所有实体创建加载票。
>
> 你也可以用逗号拼接多个实体名，例如`Player,Item`，则玩家和物品通过时均不会创建加载票。

### `OnlyPlayerCanCreateNeatherPortalFrame` 规则

类型：`Boolean`

默认值: `false`

> 防止非玩家实体创建下界传送门导致极少数情况下串门

> 这个问题在NRY 1.21.6服务器上史莱姆切门农场长时间被发现有概率在正下方串个门出来的问题

> 推测存在某种原因使史莱姆传送时有极小概率不会检测到过高位置的传送门（NRY服务器中史莱姆门在基岩上层230位置），导致游戏会在原门**正**下方（基岩层下）创建传送门框架

> 设为`true`则回退24w21a之前的行为，只有玩家实体可以创建下界传送门框架，若不存在对应传送门框架则拒绝传送实体。



## Contributions

Original mod by Crystal0404:[Crystal Carpet Additon](https://github.com/Crystal0404/CrystalCarpetAddition)

## License

GPLv3