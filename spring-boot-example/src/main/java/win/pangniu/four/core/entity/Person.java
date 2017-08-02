package win.pangniu.four.core.entity;

/**
 * 测试实体
 * 
 * @author fourwenwen
 *
 */
public class Person {

	private String id;

	private String name;

	private int age;

	private String gender;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "id:" + id + ",name" + name + ",age=" + age + ",gender=" + gender;
	}

}
