package win.pangniu.four.data;

import win.pangniu.four.utils.HashingUtils;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash的实现，也就是hash环。
 *
 * @param <T>
 * @author fourwenwen
 */
public class HashWheel<T> { // T类封装了缓存对象，可操作也行，服务器信息也行

    private TreeMap<Long, T> nodes; // 虚拟节点
    private List<T> hotspots; // 真实缓存对象节点
    private int virtual_node_count = 150; // 每个缓存对象节点关联的虚拟节点个数，建议是150个

    public HashWheel(List<T> hotspots) {
        this(hotspots, 150);
    }

    public HashWheel(List<T> hotspots, int virtualNodeCount) {
        super();
        this.hotspots = hotspots;
        virtual_node_count = virtualNodeCount;
        init();
    }

    private void init() {
        nodes = new TreeMap<Long, T>();
        for (int i = 0; i != hotspots.size(); ++i) { // 每个真实缓存对象节点都需要关联虚拟节点
            final T hotspotInfo = hotspots.get(i);
            for (int n = 0; n < virtual_node_count; n++) {
                // 一个真实机器节点关联NODE_NUM个虚拟节点
                nodes.put(HashingUtils.hash("hotspot-" + i + "-node-" + n), hotspotInfo);
            }
        }
    }

    /**
     * 获取轮子中最接近这个key的热点。
     *
     * @param key
     * @return
     */
    public T getHotspotsInfo(String key) {
        // 沿环的顺时针找到一个虚拟节点。
        SortedMap<Long, T> tail = nodes.tailMap(HashingUtils.hash(key));
        if (tail.size() == 0) {
            return nodes.get(nodes.firstKey());
        }
        return tail.get(tail.firstKey()); // 返回该虚拟节点对应的真实缓存对象节点的信息
    }

    /**
     * 获取轮子中最接近key中第num个热点
     *
     * @param key
     * @param num
     * @return
     */
    public T getHotspotsInfoByNum(String key, int num) {
        // 沿环的顺时针找到一个虚拟节点。
        SortedMap<Long, T> tail = nodes.tailMap(HashingUtils.hash(key));
        if (tail.size() == 0) {
            return nodes.get(nodes.firstKey());
        }
        int i = 1;
        for (T entry : tail.values()) {
            if (i == num) {
                return entry;
            }
            i++;
        }
        return tail.get(tail.firstKey()); // 返回该虚拟节点对应的真实缓存对象节点的信息
    }

    public List<T> getHotspots() {
        return hotspots;
    }

}
