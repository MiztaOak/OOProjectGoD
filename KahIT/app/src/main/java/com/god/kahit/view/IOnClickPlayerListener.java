package com.god.kahit.view;

import com.god.kahit.model.Player;

/**
 * responsibility: Works as a clickListener for a player-row in multiplayer.
 * <p>
 * used-by: LobbyNetRecyclerAdapter, LobbyNetView, TeamContainerRecyclerAdapter.
 *
 * @author Mats Cedervall
 */

public interface IOnClickPlayerListener {
    void onClick(Player player);
}
