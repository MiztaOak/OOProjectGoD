package com.god.kahit.view;

import com.god.kahit.networkManager.Connection;

/**
 * responsibility: Works as a clickListener for the different rooms in the JoinLobbyNetRecyclerAdapter.
 * <p>
 * used-by: JoinLobbyNetRecyclerAdapter, JoinLobbyNetView.
 *
 * @author Mats Cedervall
 */
interface IOnClickLobbyListener {
    void onClick(Connection roomConnection);
}
