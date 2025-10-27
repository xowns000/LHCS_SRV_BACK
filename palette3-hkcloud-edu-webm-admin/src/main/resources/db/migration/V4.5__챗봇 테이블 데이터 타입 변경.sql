ALTER TABLE chatbot_history ALTER COLUMN "content" TYPE text USING "content"::text;


ALTER TABLE chatbot_card_button
	ADD CONSTRAINT chatbot_card_button_fk FOREIGN KEY (block_id,response_id,card_id) REFERENCES chatbot_response_card(block_id,response_id,card_id);
	
ALTER TABLE chatbot_quick_button
	ADD CONSTRAINT chatbot_quick_button_fk FOREIGN KEY (block_id) REFERENCES chatbot_block(block_id);
	
ALTER TABLE chatbot_response
	ADD CONSTRAINT chatbot_response_fk FOREIGN KEY (block_id) REFERENCES chatbot_block(block_id);
	
ALTER TABLE chatbot_response_card
	ADD CONSTRAINT chatbot_response_card_fk FOREIGN KEY (block_id,response_id) REFERENCES chatbot_response(block_id,response_id);
	
ALTER TABLE chatbot_utterance
	ADD CONSTRAINT chatbot_utterance_fk FOREIGN KEY (block_id) REFERENCES chatbot_block(block_id);