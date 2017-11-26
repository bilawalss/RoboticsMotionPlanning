# RoboticSimulator (in progress)

A simulator for motion planning algorithms. 

To run the application:
gradle run -PappArgs="['environment_file_path', 'config_file_path']"

For instance, to run the application with the example environment and RRT files:
gradle run -PappArgs="['./environment.txt'", './rrt_config.txt']"
