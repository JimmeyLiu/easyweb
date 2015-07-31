package model

import org.nutz.dao.entity.annotation.Column
import org.nutz.dao.entity.annotation.Id
import org.nutz.dao.entity.annotation.Name
import org.nutz.dao.entity.annotation.Table

/**
 * Created by jimmey on 15-7-30.
 */
@Table("test")
class Hello {
    @Id
    @Column
    Integer id;

    @Column
    String name;

    @Column
    int age;

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    int getAge() {
        return age
    }

    void setAge(int age) {
        this.age = age
    }

    Integer getId() {
        return id
    }

    void setId(Integer id) {
        this.id = id
    }
}
