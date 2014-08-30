#include "ShaderProgram.h"
using namespace std;

namespace hidrogine {

	ShaderProgram::ShaderProgram(string vsFilename, string fsFilename)
	{
		//Read our shaders into the appropriate buffers
		string vertexSource = Utils::fileToString(vsFilename);
		string fragmentSource = Utils::fileToString(fsFilename);

		//Create an empty vertex shader handle
		GLuint vertexShader = glCreateShader(GL_VERTEX_SHADER);

		//Send the vertex shader source code to GL
		//Note that string's .c_str is NULL character terminated.
		const GLchar *source = (const GLchar *)vertexSource.c_str();
		glShaderSource(vertexShader, 1, &source, 0);

		//Compile the vertex shader
		glCompileShader(vertexShader);
			
		GLint isCompiled = 0;
		glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &isCompiled);
		if(isCompiled == GL_FALSE)
		{
			GLint maxLength = 0;
			glGetShaderiv(vertexShader, GL_INFO_LOG_LENGTH, &maxLength);
			 
			//The maxLength includes the NULL character
			vector<GLchar> infoLog(maxLength);
			glGetShaderInfoLog(vertexShader, maxLength, &maxLength, &infoLog[0]);
			 
			//We don't need the shader anymore.
			glDeleteShader(vertexShader);
			 
			//Use the infoLog as you see fit.
			cout << string(infoLog.begin(),infoLog.end()) << endl;
			//In this simple program, we'll just leave
			return;
		}
			 
		//Create an empty fragment shader handle
		GLuint fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
			 
		//Send the fragment shader source code to GL
		//Note that string's .c_str is NULL character terminated.
		source = (const GLchar *)fragmentSource.c_str();
		glShaderSource(fragmentShader, 1, &source, 0);
			 
		//Compile the fragment shader
		glCompileShader(fragmentShader);
			 
		glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &isCompiled);
		if(isCompiled == GL_FALSE)
		{
			GLint maxLength = 0;
			glGetShaderiv(fragmentShader, GL_INFO_LOG_LENGTH, &maxLength);
			 
			//The maxLength includes the NULL character
			vector<GLchar> infoLog(maxLength);
			glGetShaderInfoLog(fragmentShader, maxLength, &maxLength, &infoLog[0]);
			 
			//We don't need the shader anymore.
			glDeleteShader(fragmentShader);
			//Either of them. Don't leak shaders.
			glDeleteShader(vertexShader);
			 
			//Use the infoLog as you see fit.
			cout << string(infoLog.begin(),infoLog.end()) << endl;
			//In this simple program, we'll just leave
			return;
		}
			 
		//Vertex and fragment shaders are successfully compiled.
		//Now time to link them together into a program.
		//Get a program object.
		program = glCreateProgram();
			 
		//Attach our shaders to our program
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
			 
		//Link our program
		glLinkProgram(program);
			 
		//Note the different functions here: glGetProgram* instead of glGetShader*.
		GLint isLinked = 0;
		glGetProgramiv(program, GL_LINK_STATUS, (int *)&isLinked);
		if(isLinked == GL_FALSE)
		{
			GLint maxLength = 0;
			glGetProgramiv(program, GL_INFO_LOG_LENGTH, &maxLength);
			 
			//The maxLength includes the NULL character
			vector<GLchar> infoLog(maxLength);
			glGetProgramInfoLog(program, maxLength, &maxLength, &infoLog[0]);
			 
			//We don't need the program anymore.
			glDeleteProgram(program);
			//Don't leak shaders either.
			glDeleteShader(vertexShader);
			glDeleteShader(fragmentShader);
			 
			//Use the infoLog as you see fit.
			cout << string(infoLog.begin(),infoLog.end()) << endl;
			//In this simple program, we'll just leave
			return;
		}
			 
		//Always detach shaders after a successful link.
		glDetachShader(program, vertexShader);
		glDetachShader(program, fragmentShader);

		glUseProgram(program);

		// Get matrices uniform locations
        projectionMatrixLocation = glGetUniformLocation(program, "projectionMatrix");
        viewMatrixLocation = glGetUniformLocation(program, "viewMatrix");
        modelMatrixLocation = glGetUniformLocation(program, "modelMatrix");
        ambientColorLocation = glGetUniformLocation(program, "ambientColor");
        timeLocation = glGetUniformLocation(program, "ftime");

        diffuseColorLocation = glGetUniformLocation(program, "diffuseColor");
        cameraPositionLocation = glGetUniformLocation(program, "cameraPosition");
        opaqueLocation = glGetUniformLocation(program, "opaque");

        // material locations
        materialShininessLocation = glGetUniformLocation(program, "materialShininess");
        materialAlphaLocation = glGetUniformLocation(program, "materialAlpha");
        materialSpecularLocation = glGetUniformLocation(program,"materialSpecular");

        for (int i = 0; i < 10; ++i) {
			lightPositionLocation[i] = glGetUniformLocation(program,("lightPosition[" + to_string(i) + "]").c_str() );
            lightSpecularColorLocation[i] = glGetUniformLocation(program, ("lightSpecularColor[" + to_string(i) + "]").c_str());
        }
        setMaterialAlpha(1.0f);

	}

	
	void ShaderProgram::setMaterialAlpha(float value) {

        glUniform1fARB(materialAlphaLocation, value);
    }


	ShaderProgram::~ShaderProgram(void)
	{
	}











}