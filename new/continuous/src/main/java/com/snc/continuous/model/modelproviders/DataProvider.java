package com.mangione.continuous.model.modelproviders;

public interface DataProvider<S> {

	int getNumberOfLines();

	S get(int row);

	int getLengthOfObservation();


}
