#ifndef FRAME_UTIL
#define FRAME_UTIL

#include <vector>

class  Frame
{
	public: std::vector<char> currentFrame;
	public: std::vector<char> oldFrame;
	public: long age;

	public: Frame(){
		age = 0;
	}


	public: void setFrame(std::vector<char> frame){
		if(age % 300 == 0){
			oldFrame = age == 0 ? frame : currentFrame;
		}
		currentFrame = frame;		
		++age;
	}
	
};





#endif



