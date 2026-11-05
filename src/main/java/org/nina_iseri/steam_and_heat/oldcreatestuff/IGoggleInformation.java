package org.nina_iseri.steam_and_heat.oldcreatestuff;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.network.chat.Component;

public interface IGoggleInformation extends IHaveGoggleInformation {

    String spacing = "    ";


    Component componentSpacing = Component.literal(spacing);

}
