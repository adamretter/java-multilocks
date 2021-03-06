/**
 * Copyright © 2010, Khilan Gudka
 * All rights reserved.
 *
 * Modifications and additions from the original source code at
 * https://github.com/kgudka/java-multilocks are
 * Copyright © 2017, Evolved Binary
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package uk.ac.ic.doc.slurp.multilock;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.ac.ic.doc.slurp.multilock.LockMode.*;

/**
 * Uses a lock hierarchy of two levels, root and child
 * to test what lock acquisitions may be made on the child
 * in the presence of locks on the root.
 *
 * Functions named like {@code t1_root_IS_t2_child_IX}
 * should be read as, Test where:
 *     Thread 1 has IS lock on root,
 *     and Thread 2 will attempt IX lock on child.
 *
 * There are two types of tests here:
 *
 * 1. Single threaded tests which check what modes the accessor
 *     thread is allowed to take on the child after locking the root.
 *
 * 2. Bi-threaded tests where thread t1 locks the root, and then
 *     thread t2 tries to lock the child.
 *
 * @author Adam Retter <adam@evolvedbinary.com>
 */
public class HierarchyTest {

    private static final long LOCK_ACQUISITION_TIMEOUT = 20;        // TODO(AR) this might need to be longer on slower machines...

    /* single threaded tests below */

    @Test
    public void t1_root_IS_t1_child_IS() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(IS, IS);
    }

    @Test
    public void t1_root_IS_t1_child_IS_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(IS, IS);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_IS_t1_child_IX() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChild(IS, IX);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_IS_t1_child_IX_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChildInterruptibly(IS, IX);
    }

    @Test
    public void t1_root_IS_t1_child_S() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(IS, S);
    }

    @Test
    public void t1_root_IS_t1_child_S_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(IS, S);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_IS_t1_child_SIX() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChild(IS, SIX);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_IS_t1_child_SIX_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChildInterruptibly(IS, SIX);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_IS_t1_child_X() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChild(IS, X);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_IS_t1_child_X_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChildInterruptibly(IS, X);
    }

    @Test
    public void t1_root_IX_t1_child_IS() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(IX, IS);
    }

    @Test
    public void t1_root_IX_t1_child_IS_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(IX, IS);
    }

    @Test
    public void t1_root_IX_t1_child_IX() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(IX, IX);
    }

    @Test
    public void t1_root_IX_t1_child_IX_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(IX, IX);
    }

    @Test
    public void t1_root_IX_t1_child_S() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(IX, S);
    }

    @Test
    public void t1_root_IX_t1_child_S_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(IX, S);
    }

    @Test
    public void t1_root_IX_t1_child_SIX() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(IX, SIX);
    }

    @Test
    public void t1_root_IX_t1_child_SIX_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(IX, SIX);
    }

    @Test
    public void t1_root_IX_t1_child_X() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(IX, X);
    }

    @Test
    public void t1_root_IX_t1_child_X_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(IX, X);
    }

    //TODO(AR) need tests for t1_root_S_child... -- I think it's likely that S on root should only be able to get S on child

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_SIX_t1_child_IS() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChild(SIX, IS);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_SIX_t1_child_IS_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChildInterruptibly(SIX, IS);
    }

    @Test
    public void t1_root_SIX_t1_child_IX() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(SIX, IX);
    }

    @Test
    public void t1_root_SIX_t1_child_IX_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(SIX, IX);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_SIX_t1_child_S() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChild(SIX, S);
    }

    @Disabled("TODO - page 7 of Gray's paper - Granularity of Locks in a Shared Data Base")
    @Test
    public void t1_root_SIX_t1_child_S_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenNotChildInterruptibly(SIX, S);
    }

    @Test
    public void t1_root_SIX_t1_child_SIX() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(SIX, SIX);
    }

    @Test
    public void t1_root_SIX_t1_child_SIX_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(SIX, SIX);
    }

    @Test
    public void t1_root_SIX_t1_child_X() throws ExecutionException, InterruptedException {
        assertLockRootThenChild(SIX, X);
    }

    @Test
    public void t1_root_SIX_t1_child_X_interruptibly() throws ExecutionException, InterruptedException {
        assertLockRootThenChildInterruptibly(SIX, X);
    }

    //TODO(AR) need tests for t1_root_X_child... -- I think it's likely that X on root should only be able to get X on child


    /* bi-threaded tests below */

    @Test
    public void t1_root_IS_t2_child_IS() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, IS);

    }

    @Test
    public void t1_root_IS_t2_child_IS_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, IS);

    }

    @Test
    public void t1_root_IS_t2_child_IX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, IX);
    }

    @Test
    public void t1_root_IS_t2_child_IX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, IX);
    }

    @Test
    public void t1_root_IS_t2_child_S() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, S);
    }

    @Test
    public void t1_root_IS_t2_child_S_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, S);
    }

    @Test
    public void t1_root_IS_t2_child_SIX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, SIX);
    }

    @Test
    public void t1_root_IS_t2_child_SIX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, SIX);
    }

    @Test
    public void t1_root_IS_t2_child_X() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, X);
    }

    @Test
    public void t1_root_IS_t2_child_X_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IS.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, X);
    }

    @Test
    public void t1_root_IX_t2_child_IS() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, IS);

    }

    @Test
    public void t1_root_IX_t2_child_IS_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, IS);

    }

    @Test
    public void t1_root_IX_t2_child_IX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, IX);
    }

    @Test
    public void t1_root_IX_t2_child_IX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, IX);
    }

    @Test
    public void t1_root_IX_t2_child_S() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, S);
    }

    @Test
    public void t1_root_IX_t2_child_S_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, S);
    }

    @Test
    public void t1_root_IX_t2_child_SIX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, SIX);
    }

    @Test
    public void t1_root_IX_t2_child_SIX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, SIX);
    }

    @Test
    public void t1_root_IX_t2_child_X() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, SIX);
    }

    @Test
    public void t1_root_IX_t2_child_X_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        IX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, SIX);
    }

    @Test
    public void t1_root_S_t2_child_IS() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, IS);
    }

    @Test
    public void t1_root_S_t2_child_IS_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, IS);
    }

    @Test
    public void t1_root_S_t2_child_IX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S on root, other thread attempts IX on child (which attempts IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChild(child, IX);
    }

    @Test
    public void t1_root_S_t2_child_IX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S on root, other thread attempts IX on child (which attempts IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChildInterruptibly(child, IX);
    }

    @Test
    public void t1_root_S_t2_child_S() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, S);
    }

    @Test
    public void t1_root_S_t2_child_S_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, S);
    }

    @Test
    public void t1_root_S_t2_child_SIX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S on root, other thread attempts SIX on child (which attempts S+IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChild(child, SIX);
    }

    @Test
    public void t1_root_S_t2_child_SIX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S on root, other thread attempts SIX on child (which attempts S+IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChildInterruptibly(child, SIX);
    }

    @Test
    public void t1_root_S_t2_child_X() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S on root, other thread attempts X on child (which attempts IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChild(child, X);
    }

    @Test
    public void t1_root_S_t2_child_X_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        S.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S on root, other thread attempts X on child (which attempts IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChildInterruptibly(child, X);
    }

    @Test
    public void t1_root_SIX_t2_child_IS() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, IS);
    }

    @Test
    public void t1_root_SIX_t2_child_IS_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, IS);
    }

    @Test
    public void t1_root_SIX_t2_child_IX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S+IX on root, other thread attempts IX on child (which attempts IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChild(child, IX);
    }

    @Test
    public void t1_root_SIX_t2_child_IX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S+IX on root, other thread attempts IX on child (which attempts IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChildInterruptibly(child, IX);
    }

    @Test
    public void t1_root_SIX_t2_child_S() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChild(child, S);
    }

    @Test
    public void t1_root_SIX_t2_child_S_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        assertOtherThreadLockableChildInterruptibly(child, S);
    }

    @Test
    public void t1_root_SIX_t2_child_SIX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S+IX on root, other thread attempts SIX on child (which attempts IS+IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChild(child, SIX);
    }

    @Test
    public void t1_root_SIX_t2_child_SIX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S+IX on root, other thread attempts SIX on child (which attempts IS+IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChildInterruptibly(child, SIX);
    }

    @Test
    public void t1_root_SIX_t2_child_X() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S+IX on root, other thread attempts X on child (which attempts IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChild(child, X);
    }

    @Test
    public void t1_root_SIX_t2_child_X_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        SIX.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds S+IX on root, other thread attempts X on child (which attempts IX on root). S is not compatible with IX
        assertOtherThreadNotLockableChildInterruptibly(child, X);
    }

    @Test
    public void t1_root_X_t2_child_IS() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts IS on child (which attempts IS on root). X is not compatible with IS
        assertOtherThreadNotLockableChild(child, IS);
    }

    @Test
    public void t1_root_X_t2_child_IS_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts IS on child (which attempts IS on root). X is not compatible with IS
        assertOtherThreadNotLockableChildInterruptibly(child, IS);
    }

    @Test
    public void t1_root_X_t2_child_IX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts IX on child (which attempts IX on root). X is not compatible with IX
        assertOtherThreadNotLockableChild(child, IX);
    }

    @Test
    public void t1_root_X_t2_child_IX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts IX on child (which attempts IX on root). X is not compatible with IX
        assertOtherThreadNotLockableChildInterruptibly(child, IX);
    }

    @Test
    public void t1_root_X_t2_child_S() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts S on child (which attempts IS on root). X is not compatible with IS
        assertOtherThreadNotLockableChild(child, S);
    }

    @Test
    public void t1_root_X_t2_child_S_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts S on child (which attempts IS on root). X is not compatible with IS
        assertOtherThreadNotLockableChildInterruptibly(child, S);
    }

    @Test
    public void t1_root_X_t2_child_SIX() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts SIX on child (which attempts IS+IX on root). X is not compatible with IS or IX
        assertOtherThreadNotLockableChild(child, SIX);
    }

    @Test
    public void t1_root_X_t2_child_SIX_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts SIX on child (which attempts IS+IX on root). X is not compatible with IS or IX
        assertOtherThreadNotLockableChildInterruptibly(child, SIX);
    }

    @Test
    public void t1_root_X_t2_child_X() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lock(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts X on child (which attempts IX on root). X is not compatible with IX
        assertOtherThreadNotLockableChild(child, X);
    }

    @Test
    public void t1_root_X_t2_child_X_interruptibly() throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        X.lockInterruptibly(root);

        final MultiLock child = new HierarchicalMultiLock(root);
        // notLockable = test thread holds X on root, other thread attempts X on child (which attempts IX on root). X is not compatible with IX
        assertOtherThreadNotLockableChildInterruptibly(child, X);
    }

    private static void assertLockRootThenChild(final LockMode rootMode, final LockMode childMode) throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        final MultiLock child = new HierarchicalMultiLock(root);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final List<Future<Boolean>> futures = executorService.invokeAll(Arrays.asList(new RootAndChildLockAcquirer(root, rootMode, child, childMode)), LOCK_ACQUISITION_TIMEOUT, TimeUnit.MILLISECONDS);

        for (final Future<Boolean> future : futures) {
            assertTrue(future.isDone());

            assertFalse(future.isCancelled());

            assertTrue(future.get());
        }
    }

    private static void assertLockRootThenChildInterruptibly(final LockMode rootMode, final LockMode childMode) throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        final MultiLock child = new HierarchicalMultiLock(root);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final List<Future<Boolean>> futures = executorService.invokeAll(Arrays.asList(new RootAndChildLockInterruptiblyAcquirer(root, rootMode, child, childMode)), LOCK_ACQUISITION_TIMEOUT, TimeUnit.MILLISECONDS);

        for (final Future<Boolean> future : futures) {
            assertTrue(future.isDone());

            assertFalse(future.isCancelled());

            assertTrue(future.get());
        }
    }

    private static void assertLockRootThenNotChild(final LockMode rootMode, final LockMode childMode) throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        final MultiLock child = new HierarchicalMultiLock(root);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final List<Future<Boolean>> futures = executorService.invokeAll(Arrays.asList(new RootAndChildLockAcquirer(root, rootMode, child, childMode)), LOCK_ACQUISITION_TIMEOUT, TimeUnit.MILLISECONDS);

        for (final Future<Boolean> future : futures) {
            assertTrue(future.isDone());

            assertTrue(future.isCancelled());
        }
    }

    private static void assertLockRootThenNotChildInterruptibly(final LockMode rootMode, final LockMode childMode) throws InterruptedException, ExecutionException {
        final MultiLock root = new MultiLock();
        final MultiLock child = new HierarchicalMultiLock(root);

        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final List<Future<Boolean>> futures = executorService.invokeAll(Arrays.asList(new RootAndChildLockInterruptiblyAcquirer(root, rootMode, child, childMode)), LOCK_ACQUISITION_TIMEOUT, TimeUnit.MILLISECONDS);

        for (final Future<Boolean> future : futures) {
            assertTrue(future.isDone());

            assertTrue(future.isCancelled());
        }
    }

    private static void assertOtherThreadLockableChild(final MultiLock child, final LockMode childMode) throws InterruptedException, ExecutionException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final List<Future<Boolean>> futures = executorService.invokeAll(Arrays.asList(new ChildLockAcquirer(child, childMode)), LOCK_ACQUISITION_TIMEOUT, TimeUnit.MILLISECONDS);

        for (final Future<Boolean> future : futures) {
            assertTrue(future.isDone());
            assertFalse(future.isCancelled());

            assertTrue(future.get());
        }
    }

    private static void assertOtherThreadLockableChildInterruptibly(final MultiLock child, final LockMode childMode) throws InterruptedException, ExecutionException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final List<Future<Boolean>> futures = executorService.invokeAll(Arrays.asList(new ChildLockInterruptiblyAcquirer(child, childMode)), LOCK_ACQUISITION_TIMEOUT, TimeUnit.MILLISECONDS);

        for (final Future<Boolean> future : futures) {
            assertTrue(future.isDone());
            assertFalse(future.isCancelled());

            assertTrue(future.get());
        }
    }

    private static void assertOtherThreadNotLockableChild(final MultiLock child, final LockMode childMode) throws InterruptedException, ExecutionException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final List<Future<Boolean>> futures = executorService.invokeAll(Arrays.asList(new ChildLockAcquirer(child, childMode)), LOCK_ACQUISITION_TIMEOUT, TimeUnit.MILLISECONDS);

        for (final Future<Boolean> future : futures) {
            assertTrue(future.isDone());

            assertTrue(future.isCancelled());
        }
    }

    private static void assertOtherThreadNotLockableChildInterruptibly(final MultiLock child, final LockMode childMode) throws InterruptedException, ExecutionException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final List<Future<Boolean>> futures = executorService.invokeAll(Arrays.asList(new ChildLockInterruptiblyAcquirer(child, childMode)), LOCK_ACQUISITION_TIMEOUT, TimeUnit.MILLISECONDS);

        for (final Future<Boolean> future : futures) {
            assertTrue(future.isDone());

            assertTrue(future.isCancelled());
        }
    }

    private static class RootAndChildLockAcquirer implements Callable<Boolean> {
        private final MultiLock root;
        private final LockMode rootLockMode;
        private final MultiLock child;
        private final LockMode childLockMode;

        public RootAndChildLockAcquirer(final MultiLock root, final LockMode rootLockMode, final MultiLock child, final LockMode childLockMode) {
            this.root = root;
            this.rootLockMode = rootLockMode;
            this.child = child;
            this.childLockMode = childLockMode;
        }

        @Override
        public Boolean call() {
            rootLockMode.lock(root);
            childLockMode.lock(child);
            return true;
        }
    }

    private static class RootAndChildLockInterruptiblyAcquirer implements Callable<Boolean> {
        private final MultiLock root;
        private final LockMode rootLockMode;
        private final MultiLock child;
        private final LockMode childLockMode;

        public RootAndChildLockInterruptiblyAcquirer(final MultiLock root, final LockMode rootLockMode, final MultiLock child, final LockMode childLockMode) {
            this.root = root;
            this.rootLockMode = rootLockMode;
            this.child = child;
            this.childLockMode = childLockMode;
        }

        @Override
        public Boolean call() throws InterruptedException {
            rootLockMode.lockInterruptibly(root);
            childLockMode.lockInterruptibly(child);
            return true;
        }
    }

    private static class ChildLockAcquirer implements Callable<Boolean> {
        private final MultiLock child;
        private final LockMode lockMode;

        public ChildLockAcquirer(final MultiLock child, final LockMode lockMode) {
            this.child = child;
            this.lockMode = lockMode;
        }

        @Override
        public Boolean call() {
            lockMode.lock(child);
            return true;
        }
    }

    private static class ChildLockInterruptiblyAcquirer implements Callable<Boolean> {
        private final MultiLock child;
        private final LockMode lockMode;

        public ChildLockInterruptiblyAcquirer(final MultiLock child, final LockMode lockMode) {
            this.child = child;
            this.lockMode = lockMode;
        }

        @Override
        public Boolean call() throws InterruptedException {
            lockMode.lockInterruptibly(child);
            return true;
        }
    }
}
