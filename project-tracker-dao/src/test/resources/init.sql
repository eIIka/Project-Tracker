--
--
--
-- schema
--
--
--
-- table for users
create table users
(
    id INT primary key auto_increment,
    nickname VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(255) default '' null,
    last_name VARCHAR(255) default '' null,
    phone_number VARCHAR(20),
    email VARCHAR(200),
    password VARCHAR(200),
    user_role ENUM ('Manager', 'Employee') not null default 'Employee',
    registered_at DATETIME not null default CURRENT_TIMESTAMP,
    last_login_at DATETIME default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    constraint users_email_unique unique (email),
    constraint users_phone_unique unique (phone_number)
);

create index users_role_idx on users (user_role);

-- table for project
create table project
(
    id INT primary key auto_increment,
    name VARCHAR(200) not null,
    description TEXT not null,
    priority INT not null check (priority between 1 and 10),
    status ENUM ('Planned', 'In Progress', 'Completed', 'On Hold', 'Archived') not null default 'Planned',
    created_at DATETIME not null default CURRENT_TIMESTAMP,
    update_at DATETIME default null,
    end_date DATE default null,
    deadline DATE default null,
    manager_id INT,
    constraint project_manager_fk
        foreign key (manager_id) references users (id) on delete set null
);

create index project_manager_id_idx on project (manager_id);

-- table for project_employees
create table project_employees
(
    project_id  INT not null,
    employee_id INT not null,
    primary key (project_id, employee_id),
    constraint project_employees_project_fk
        foreign key (project_id) references project (id) on delete cascade,
    constraint project_employees_employee_fk
        foreign key (employee_id) references users (id) on delete cascade
);


-- table for tasks
create table task
(
    id INT primary key auto_increment,
    name VARCHAR(255) not null,
    description TEXT,
    pending_status ENUM ('Not Started', 'TODO', 'In Progress', 'Needs Review', 'Blocked', 'Completed') default null,
    execution_details TEXT,
    status ENUM ('Not Started', 'TODO', 'In Progress', 'Needs Review', 'Blocked', 'Completed') default 'Not Started',
    type VARCHAR(100),
    priority INT not null check (priority between 1 and 10),
    created_at DATETIME not null default CURRENT_TIMESTAMP,
    update_at DATETIME default null,
    end_date DATE default null,
    deadline DATE default null,
    project_id INT,
    constraint task_project
        foreign key (project_id) references project (id) on delete cascade,
    manager_id INT,
    foreign key (manager_id) references users (id) on delete set null,
    employee_id INT,
    foreign key (employee_id) references users (id) on delete set null
);

create index task_manager_id_idx on task (manager_id);
create index task_employee_id_idx on task (employee_id);
create index task_project_id_idx on task (project_id);

-- table for comments to the task
create table task_comment
(
    id INT primary key auto_increment,
    task_id INT not null,
    comment TEXT not null,
    created_at DATETIME not null default CURRENT_TIMESTAMP,
    constraint task_comment_task_fk
        foreign key (task_id) references task (id) on delete cascade
);

create index task_comment_task_id_idx on task_comment (task_id);

-- table of task history
create table task_history
(
    id INT primary key auto_increment,
    task_id INT not null,
    history_entry TEXT not null,
    created_at DATETIME not null default CURRENT_TIMESTAMP,
    constraint task_history_fk
        foreign key (task_id) references task (id) on delete cascade
);

create index task_history_task_id_idx on task_history (task_id);

-- table of profiles
create table profile
(
    id INT primary key auto_increment,
    avatar_url VARCHAR(1024),
    user_id INT,
    constraint profile_user_unique unique (user_id),
    constraint profile_user_fk
        foreign key (user_id) references users (id) on delete cascade
);

--
--
--
-- data
--
--
--
INSERT INTO users (nickname, first_name, last_name, phone_number, email, password, user_role)
VALUES ('employee0', 'Admin', 'User', '1234567890', 'admin@example.com', 'admin_pass', 'Employee'),
       ('manager1', 'John', 'Doe', '2345678901', 'manager1@example.com', 'manager_pass1', 'Manager'),
       ('ellka', 'Vlad', 'Kalkatin', '0688228575', 'ellka@gmail.com', 'manager_pass2', 'Manager'),
       ('employee1', 'Michael', 'Johnson', '4567890123', 'employee1@example.com', 'employee_pass1', 'Employee'),
       ('employee2', 'Emily', 'Davis', '5678901234', 'employee2@example.com', 'employee_pass2', 'Employee');

INSERT INTO project (name, description, priority, status, manager_id)
VALUES ('Project Alpha', 'This is the first project', 7, 'Planned', 2),
       ('Project Beta', 'Second project with higher priority', 9, 'In Progress', 3),
       ('Project Gamma', 'An ongoing project with some issues', 5, 'On Hold', 2),
       ('Project Delta', 'A low priority project', 4, 'Planned', 3),
       ('Project Epsilon', 'Final project for the quarter', 8, 'Completed', 2),
       ('Project 12', 'Project for the quarter',10, 'Completed', 2);

INSERT INTO project_employees (project_id, employee_id)
VALUES (1, 4),
       (1, 5),
       (2, 4),
       (3, 5),
       (4, 5);

INSERT INTO task (name, description, pending_status, execution_details, status, type, priority, project_id, manager_id,
                  employee_id)
VALUES ('Task 1', 'Task for project Alpha', 'In Progress', 'Working on initial planning', 'In Progress', 'Development',
        7, 1, 2, 4),
       ('Task 2', 'Task for project Beta', 'Needs Review', 'Reviewing the initial prototype', 'Needs Review', 'Testing',
        9, 2, 3, 5),
       ('Task 3', 'Task for project Gamma', 'Not Started', 'Waiting for manager approval', 'Not Started', 'Research', 5,
        3, 2, 5),
       ('Task 4', 'Task for project Delta', 'Blocked', 'Waiting for resources', 'Blocked', 'Design', 4, 4, 3, 4),
       ('Task 5', 'Task for project Epsilon', 'Completed', 'Task finished and delivered', 'Completed', 'Development', 8,
        5, 2, 4);

INSERT INTO task_comment (task_id, comment)
VALUES (1, 'This task is progressing well.'),
       (2, 'Waiting for feedback on the testing results.'),
       (3, 'Research phase is delayed due to external factors.'),
       (4, 'Resources are being gathered for design.'),
       (5, 'Task completed on schedule.');

INSERT INTO task_history (task_id, history_entry)
VALUES (1, 'Task created and assigned to employee.'),
       (2, 'Task moved to review phase.'),
       (3, 'Task created and employee assigned.'),
       (4, 'Task is blocked due to resource shortage.'),
       (5, 'Task marked as completed.');

INSERT INTO profile (avatar_url, user_id)
VALUES ('https://example.com/avatar1.jpg', 1),
       ('https://example.com/avatar2.jpg', 2),
       ('https://example.com/avatar3.jpg', 3),
       ('https://example.com/avatar4.jpg', 4),
       ('https://example.com/avatar5.jpg', 5);
