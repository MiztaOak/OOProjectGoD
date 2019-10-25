package com.god.kahit.view;

/**
 * responsibility: Works as a clickListener in the HotSwapRecyclerView.
 * <p>
 * used-by: HotSwapRecyclerAdapter, HotSwapAddPlayersView.
 *
 * @author Jakob Ewerstrand
 */
interface IHotSwapViewHolderClickListener {

    /**
     * When adding a new player.
     */
    void onAddPlayer();

    /**
     * When a player wants to change team.
     *
     * @param position   -the players position(index) in the recyclerView.
     * @param newTeamNum -the new teamNumber.
     */
    void onTeamSelected(int position, int newTeamNum);
}
