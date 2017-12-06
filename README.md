# RoboticSimulator (in progress)

A simulator for motion planning algorithms. 

To run the application: <br>
`gradle run -PappArgs="['config_file_path', 'environment_file_path']"`

For instance, to run the application with the example environment and RRT for car: <br>
`gradle run -PappArgs="['./rrt_car_config.txt', './environment.txt']"`

For instance, to run the application with the example environment and RRT for 3D: <br>
`gradle run -PappArgs="['./rrt_config.txt', './environment.txt']"`
