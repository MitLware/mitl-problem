package org.mitlware.problem.blocksworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//////////////////////////////////////////////////////////////////////

public final class BlocksWorldState {

	private final Stack< Character > tower;
	private final List< Character > blocksOnTable;
	
	///////////////////////////////
	
	public BlocksWorldState( String stack, String blocksOnTable ) {
		this.blocksOnTable = new ArrayList< Character >();
		for( int i=0; i<blocksOnTable.length(); ++i  )
			this.blocksOnTable.add( blocksOnTable.charAt( i ) );			

		// Collections.sort( this.blocksOnTable );
		
		this.tower = new Stack<Character>();
		for( int i=0; i<stack.length(); ++i  ) 
			tower.push( stack.charAt( i ) );
		
		assert invariant();
	}

	private BlocksWorldState( List< Character > blocksOnTable, Stack< Character > tower ) {
		this.blocksOnTable = blocksOnTable;
		this.tower = tower;
		
		// Collections.sort( this.blocksOnTable );		
		assert invariant();		
	}
	
	///////////////////////////////
	
	BlocksWorldState moveTopOfTowerToTable() {
		if( towerHeight() == 0 )
			throw new IllegalArgumentException();

		Stack<Character> newTower = new Stack<Character>();
		newTower.addAll( tower );
		
		List< Character > newBlocksOnTable = new ArrayList< Character >();
		newBlocksOnTable.addAll( blocksOnTable );
		newBlocksOnTable.add( newTower.pop() );
		
		return new BlocksWorldState( newBlocksOnTable, newTower );
	}

	BlocksWorldState moveTableToTopOfTower( int tableIndex ) {
		if( tableIndex < 0 || tableIndex >= blocksOnTable.size() )
			throw new IllegalArgumentException();

		Stack<Character> newTower = new Stack<Character>();
		newTower.addAll( tower );
		newTower.push( blocksOnTable.get( tableIndex ) );
		
		List< Character > newBlocksOnTable = new ArrayList< Character >();
		newBlocksOnTable.addAll( blocksOnTable );
		newBlocksOnTable.remove( tableIndex );
		
		return new BlocksWorldState( newBlocksOnTable, newTower );
	}
	
	///////////////////////////////
	
	private int towerHeight() { return tower.size(); }
	
	public Stream< BlocksWorldState > neighbors() {
		
		List< BlocksWorldState > result = new ArrayList< BlocksWorldState >();
		for( int i=0; i<blocksOnTable.size(); ++i )
			result.add( moveTableToTopOfTower( i ) );
		
		if( towerHeight() > 0 )
			result.add( moveTopOfTowerToTable() );
		
		return result.stream();
	}
	
	public static int HammingDistance( BlocksWorldState a, BlocksWorldState b ) {
		return org.mitlware.support.util.MitlCollections.HammingDistance( a.tower, b.tower ) 
			+ org.mitlware.support.util.MitlCollections.HammingDistance( a.blocksOnTable, b.blocksOnTable );		
	}
	
	///////////////////////////////	
	
	@Override	
	public int hashCode() { return Objects.hash( blocksOnTable, tower ); }
	
	@Override
	public boolean equals( Object o ) {
		if( !( o instanceof BlocksWorldState ) )
			return false;
		
		BlocksWorldState rhs = (BlocksWorldState)o;
		return tower.equals( rhs.tower ) &&
			blocksOnTable.equals( rhs.blocksOnTable );
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString( this, ToStringStyle.SHORT_PREFIX_STYLE );
	}
	
	///////////////////////////////
	
	public boolean invariant() {
		return true; // Util.isSorted( blocksOnTable );
	}
}

// End ///////////////////////////////////////////////////////////////

